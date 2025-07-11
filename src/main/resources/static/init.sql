--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.2

-- Started on 2025-07-02 20:26:21

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 74141)
-- Name: Beer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.beer (
    type VARCHAR(100) NOT NULL,
    "ABV" real,
    description VARCHAR(255),
    id integer NOT NULL,
    name VARCHAR(255) NOT NULL,
    manufacturer_id integer NOT NULL
);


ALTER TABLE public.beer OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 74155)
-- Name: Beer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.beer ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public."Beer_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 218 (class 1259 OID 74146)
-- Name: Manufacturer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.manufacturer (
    id integer NOT NULL,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100)
);


ALTER TABLE public.manufacturer OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 74156)
-- Name: Manufacturer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.manufacturer ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public."Manufacturer_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 4897 (class 0 OID 74141)
-- Dependencies: 217
-- Data for Name: Beer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.beer (type, "ABV", description, id, name, manufacturer_id) FROM stdin;
\.


--
-- TOC entry 4898 (class 0 OID 74146)
-- Dependencies: 218
-- Data for Name: Manufacturer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.manufacturer (id, name, country) FROM stdin;
\.


--
-- TOC entry 4906 (class 0 OID 0)
-- Dependencies: 219
-- Name: Beer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Beer_id_seq"', 1, false);


--
-- TOC entry 4907 (class 0 OID 0)
-- Dependencies: 220
-- Name: Manufacturer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Manufacturer_id_seq"', 1, false);


--
-- TOC entry 4748 (class 2606 OID 74154)
-- Name: Beer Beer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.beer
    ADD CONSTRAINT "Beer_pkey" PRIMARY KEY (id);


--
-- TOC entry 4750 (class 2606 OID 74152)
-- Name: Manufacturer Manufacturer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.manufacturer
    ADD CONSTRAINT "Manufacturer_pkey" PRIMARY KEY (id);


--
-- TOC entry 4751 (class 2606 OID 74157)
-- Name: Beer manufacturer_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.beer
    ADD CONSTRAINT manufacturer_fk FOREIGN KEY (manufacturer_id) REFERENCES public.manufacturer(id) NOT VALID;


-- Completed on 2025-07-02 20:26:22

--
-- PostgreSQL database dump complete
--

