--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pgagent; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA pgagent;


ALTER SCHEMA pgagent OWNER TO postgres;

--
-- Name: SCHEMA pgagent; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA pgagent IS 'pgAgent system tables';


--
-- Name: plpython3u; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpython3u WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpython3u; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpython3u IS 'PL/Python3U untrusted procedural language';


--
-- Name: dblink; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS dblink WITH SCHEMA public;


--
-- Name: EXTENSION dblink; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION dblink IS 'connect to other PostgreSQL databases from within a database';


--
-- Name: pgagent; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgagent WITH SCHEMA pgagent;


--
-- Name: EXTENSION pgagent; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgagent IS 'A PostgreSQL job scheduler';


--
-- Name: send_notification_regarding_domain_expiry(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.send_notification_regarding_domain_expiry() RETURNS trigger
    LANGUAGE plpython3u
    AS $$import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

def send_email(domain_name, recipient_email, cc_list, days_left):
    smtp_server = "smtp.gmail.com"  # Change if necessary
    smtp_port = 587
    sender_email = "rishisarkar783@gmail.com"  # Move to environment variable
    sender_password = "zpta epxx idpt nxqp"  # Use a secure storage method like environment variables

    subject = f"🚨 Urgent: Domain Expiry Warning - {domain_name}"

    body = f"""
    <html>
    <body style="font-family: Arial, sans-serif; color: #333;">
        <p>Dear User,</p>
        <p style="font-size: 16px;">
            This is a <strong>friendly reminder</strong> that your domain <strong>{domain_name}</strong> is 
            <span style="color: red;"><b>set to expire in {days_left} days.</b></span>
        </p>
        <p>
            To avoid service disruption, please ensure timely renewal of your domain.
            If you have already initiated the renewal process, kindly ignore this message.
        </p>
        <p style="font-weight: bold;">Next Steps:</p>
        <ul>
            <li>Log in to your domain management portal.</li>
            <li>Check the renewal status of <strong>{domain_name}</strong>.</li>
            <li>Renew the domain before expiration to avoid downtime.</li>
        </ul>
        <p>
            If you need assistance, please contact our support team.
        </p>
        <p>Best regards,</p>
        <p><b>DNS Management Team</b></p>
    </body>
    </html>
    """

    msg = MIMEMultipart()
    msg["From"] = sender_email
    msg["To"] = recipient_email
    msg["Subject"] = subject
    msg.attach(MIMEText(body, "html"))  # Set content type to HTML

    # Add CC recipients
    if cc_list:
        msg["Cc"] = ", ".join(cc_list)
        recipient_email = [recipient_email] + cc_list  # Include all recipients

    try:
        server = smtplib.SMTP(smtp_server, smtp_port)
        server.starttls()  # Secure the connection
        server.login(sender_email, sender_password)
        server.sendmail(sender_email, recipient_email, msg.as_string())
        server.quit()
    except Exception as e:
        plpy.error(f"Email sending failed: {str(e)}")

# Extract domain and owner details
domain_id = TD["new"]["request_id"]
domain_name = TD["new"]["domain_name"]
drm_id = TD["new"]["drm_id"]
arm_id = TD["new"]["arm_id"]
hod_id = TD["new"]["hod_id"]

# Fetch email addresses
owner_email_result = plpy.execute(f"SELECT email FROM drm WHERE id={drm_id}")
arm_email_result = plpy.execute(f"SELECT arm_email FROM arm WHERE arm_id={arm_id}")
hod_email_result = plpy.execute(f"SELECT hod_email FROM hod WHERE hod_id={hod_id}")

# Extract email fields
owner_email = owner_email_result[0]["email"] if owner_email_result else None
arm_email = arm_email_result[0]["arm_email"] if arm_email_result else None
hod_email = hod_email_result[0]["hod_email"] if hod_email_result else None

# Determine CC recipients
cc_list = []
if TD["new"]["days_left_till_expiry"] <= 15:
    cc_list.append(arm_email)  # Add Admin
if TD["new"]["days_left_till_expiry"] <= 7:
    cc_list.append(hod_email)  # Add Support Team

# Insert into expired_domains table
if TD["new"]["days_left_till_expiry"] == 0:
    plpy.execute(f"""
        INSERT INTO expired_domains (domain_id)
        VALUES ({domain_id})
    """)

# Send email notification with CC
send_email(domain_name, owner_email, cc_list, TD["new"]["days_left_till_expiry"])

return None  # TRIGGER MUST RETURN NONE FOR AFTER UPDATE
$$;


ALTER FUNCTION public.send_notification_regarding_domain_expiry() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: app_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.app_user (
    id integer NOT NULL,
    email character varying(255),
    password character varying(255) NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.app_user OWNER TO postgres;

--
-- Name: app_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.app_user ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.app_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: arm; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.arm (
    arm_id integer NOT NULL,
    arm_email character varying(255) NOT NULL,
    arm_name character varying(255) NOT NULL,
    dept character varying(255) NOT NULL,
    hod_id integer NOT NULL
);


ALTER TABLE public.arm OWNER TO postgres;

--
-- Name: arm_arm_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.arm ALTER COLUMN arm_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.arm_arm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: dns_request; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dns_request (
    request_id integer NOT NULL,
    arm_reviewed_date date,
    date_of_expiry date,
    drm_requested_date date,
    hod_reviewed_date date,
    days_left_till_expiry bigint,
    domain_name character varying(255) NOT NULL,
    active_status boolean,
    arm_status character varying(255),
    hod_status character varying(255),
    arm_id integer,
    hod_id integer,
    drm_id integer
);


ALTER TABLE public.dns_request OWNER TO postgres;

--
-- Name: dns_request_request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.dns_request ALTER COLUMN request_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.dns_request_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: drm; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drm (
    id integer NOT NULL,
    dept character varying(255) NOT NULL,
    email character varying(255),
    name character varying(255) NOT NULL,
    arm_id integer
);


ALTER TABLE public.drm OWNER TO postgres;

--
-- Name: drm_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.drm ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.drm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: expired_domains; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expired_domains (
    id integer NOT NULL,
    domain_id integer
);


ALTER TABLE public.expired_domains OWNER TO postgres;

--
-- Name: expired_domains_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.expired_domains ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.expired_domains_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: hod; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hod (
    hod_id integer NOT NULL,
    dept character varying(255) NOT NULL,
    hod_email character varying(255),
    hod_name character varying(255) NOT NULL
);


ALTER TABLE public.hod OWNER TO postgres;

--
-- Name: hod_hod_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.hod ALTER COLUMN hod_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.hod_hod_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id integer NOT NULL,
    role_name character varying(255) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.role ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: pga_jobagent; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobagent (jagpid, jaglogintime, jagstation) FROM stdin;
\.


--
-- Data for Name: pga_jobclass; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobclass (jclid, jclname) FROM stdin;
\.


--
-- Data for Name: pga_job; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_job (jobid, jobjclid, jobname, jobdesc, jobhostagent, jobenabled, jobcreated, jobchanged, jobagentid, jobnextrun, joblastrun) FROM stdin;
\.


--
-- Data for Name: pga_schedule; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_schedule (jscid, jscjobid, jscname, jscdesc, jscenabled, jscstart, jscend, jscminutes, jschours, jscweekdays, jscmonthdays, jscmonths) FROM stdin;
\.


--
-- Data for Name: pga_exception; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_exception (jexid, jexscid, jexdate, jextime) FROM stdin;
\.


--
-- Data for Name: pga_joblog; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_joblog (jlgid, jlgjobid, jlgstatus, jlgstart, jlgduration) FROM stdin;
\.


--
-- Data for Name: pga_jobstep; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobstep (jstid, jstjobid, jstname, jstdesc, jstenabled, jstkind, jstcode, jstconnstr, jstdbname, jstonerror, jscnextrun) FROM stdin;
\.


--
-- Data for Name: pga_jobsteplog; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobsteplog (jslid, jsljlgid, jsljstid, jslstatus, jslresult, jslstart, jslduration, jsloutput) FROM stdin;
\.


--
-- Data for Name: app_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.app_user (id, email, password, role_id) FROM stdin;
1	rishisarkar234@gmail.com	1234	3
2	rishisarkar1234@gmail.com	1234	2
3	rishisarkar783@gmail.com	1234	1
\.


--
-- Data for Name: arm; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.arm (arm_id, arm_email, arm_name, dept, hod_id) FROM stdin;
1	rishisarkar1234@gmail.com	RS	CSE	1
\.


--
-- Data for Name: dns_request; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dns_request (request_id, arm_reviewed_date, date_of_expiry, drm_requested_date, hod_reviewed_date, days_left_till_expiry, domain_name, active_status, arm_status, hod_status, arm_id, hod_id, drm_id) FROM stdin;
1	2025-02-24	2025-03-26	2025-02-24	\N	25	google.com	t	VERIFIED	VERIFIED	1	1	3
\.


--
-- Data for Name: drm; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drm (id, dept, email, name, arm_id) FROM stdin;
3	CSE	rishisarkar783@gmail.com	DRM1	1
\.


--
-- Data for Name: expired_domains; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expired_domains (id, domain_id) FROM stdin;
\.


--
-- Data for Name: hod; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hod (hod_id, dept, hod_email, hod_name) FROM stdin;
1	CSE	rishisarkar234@gmail.com	SS
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, role_name) FROM stdin;
1	ROLE_DRM
2	ROLE_ARM
3	ROLE_HOD
\.


--
-- Name: app_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.app_user_id_seq', 3, true);


--
-- Name: arm_arm_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.arm_arm_id_seq', 1, false);


--
-- Name: dns_request_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.dns_request_request_id_seq', 1, true);


--
-- Name: drm_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.drm_id_seq', 3, true);


--
-- Name: expired_domains_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.expired_domains_id_seq', 1, false);


--
-- Name: hod_hod_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hod_hod_id_seq', 1, false);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 3, true);


--
-- Name: app_user app_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (id);


--
-- Name: arm arm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.arm
    ADD CONSTRAINT arm_pkey PRIMARY KEY (arm_id);


--
-- Name: dns_request dns_request_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dns_request
    ADD CONSTRAINT dns_request_pkey PRIMARY KEY (request_id);


--
-- Name: drm drm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drm
    ADD CONSTRAINT drm_pkey PRIMARY KEY (id);


--
-- Name: expired_domains expired_domains_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expired_domains
    ADD CONSTRAINT expired_domains_pkey PRIMARY KEY (id);


--
-- Name: hod hod_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hod
    ADD CONSTRAINT hod_pkey PRIMARY KEY (hod_id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: drm uk105y3rm3mjn96ay76fngdylux; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drm
    ADD CONSTRAINT uk105y3rm3mjn96ay76fngdylux UNIQUE (email);


--
-- Name: app_user uk1j9d9a06i600gd43uu3km82jw; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT uk1j9d9a06i600gd43uu3km82jw UNIQUE (email);


--
-- Name: arm uk3sr3xmrdsxuifejusu5mgriie; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.arm
    ADD CONSTRAINT uk3sr3xmrdsxuifejusu5mgriie UNIQUE (arm_email);


--
-- Name: role ukiubw515ff0ugtm28p8g3myt0h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT ukiubw515ff0ugtm28p8g3myt0h UNIQUE (role_name);


--
-- Name: hod ukjke1n80rrty75pqvns79wvcg0; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hod
    ADD CONSTRAINT ukjke1n80rrty75pqvns79wvcg0 UNIQUE (hod_email);


--
-- Name: dns_request ukkkpef6wtp7l0pib3let0axgcu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dns_request
    ADD CONSTRAINT ukkkpef6wtp7l0pib3let0axgcu UNIQUE (domain_name);


--
-- Name: expired_domains uktkc9o93obxyjt1uevieodm5bb; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expired_domains
    ADD CONSTRAINT uktkc9o93obxyjt1uevieodm5bb UNIQUE (domain_id);


--
-- Name: dns_request notify_and_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER notify_and_update_trigger AFTER UPDATE ON public.dns_request FOR EACH ROW WHEN (((new.days_left_till_expiry <= 30) AND (new.active_status = true))) EXECUTE FUNCTION public.send_notification_regarding_domain_expiry();


--
-- Name: app_user fk49hx9nj6onfot1fxtonj986ab; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT fk49hx9nj6onfot1fxtonj986ab FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: dns_request fk49sf7gxs9fqi77rt7vfeqradd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dns_request
    ADD CONSTRAINT fk49sf7gxs9fqi77rt7vfeqradd FOREIGN KEY (arm_id) REFERENCES public.arm(arm_id);


--
-- Name: arm fk5isaiiv2jngxwcvnn3b2m3u2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.arm
    ADD CONSTRAINT fk5isaiiv2jngxwcvnn3b2m3u2 FOREIGN KEY (hod_id) REFERENCES public.hod(hod_id);


--
-- Name: dns_request fk7b1myaie5jo3mg5c76b9w2040; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dns_request
    ADD CONSTRAINT fk7b1myaie5jo3mg5c76b9w2040 FOREIGN KEY (hod_id) REFERENCES public.hod(hod_id);


--
-- Name: expired_domains fk804k4d4xdla0gwxf783gl0lvi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expired_domains
    ADD CONSTRAINT fk804k4d4xdla0gwxf783gl0lvi FOREIGN KEY (domain_id) REFERENCES public.dns_request(request_id);


--
-- Name: drm fkf0efbp57eqkrhe5keg553nfc2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drm
    ADD CONSTRAINT fkf0efbp57eqkrhe5keg553nfc2 FOREIGN KEY (arm_id) REFERENCES public.arm(arm_id);


--
-- Name: dns_request fkmsiweclgjvom9lb1lo17f0lct; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dns_request
    ADD CONSTRAINT fkmsiweclgjvom9lb1lo17f0lct FOREIGN KEY (drm_id) REFERENCES public.drm(id);


--
-- PostgreSQL database dump complete
--

