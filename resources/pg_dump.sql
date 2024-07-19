--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: aircraft; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aircraft (
    id integer NOT NULL,
    model character varying(128) NOT NULL
);


ALTER TABLE public.aircraft OWNER TO postgres;

--
-- Name: aircraft_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.aircraft_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.aircraft_id_seq OWNER TO postgres;

--
-- Name: aircraft_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.aircraft_id_seq OWNED BY public.aircraft.id;


--
-- Name: airport; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.airport (
    code character(3) NOT NULL,
    country character varying(256) NOT NULL,
    city character varying(128) NOT NULL
);


ALTER TABLE public.airport OWNER TO postgres;

--
-- Name: flight; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flight (
    id bigint NOT NULL,
    flight_no character varying(16) NOT NULL,
    departure_date timestamp without time zone NOT NULL,
    departure_airport_code character(3) NOT NULL,
    arrival_date timestamp without time zone NOT NULL,
    arrival_airport_code character(3) NOT NULL,
    aircraft_id integer NOT NULL,
    status character varying(32) NOT NULL
);


ALTER TABLE public.flight OWNER TO postgres;

--
-- Name: flight_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.flight_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.flight_id_seq OWNER TO postgres;

--
-- Name: flight_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.flight_id_seq OWNED BY public.flight.id;


--
-- Name: seat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seat (
    aircraft_id integer NOT NULL,
    seat_no character varying(4) NOT NULL
);


ALTER TABLE public.seat OWNER TO postgres;

--
-- Name: ticket; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ticket (
    id bigint NOT NULL,
    passenger_no character varying(32) NOT NULL,
    passenger_name character varying(128) NOT NULL,
    flight_id bigint NOT NULL,
    seat_no character varying(4) NOT NULL,
    cost numeric(8,2) NOT NULL
);


ALTER TABLE public.ticket OWNER TO postgres;

--
-- Name: ticket_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ticket_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ticket_id_seq OWNER TO postgres;

--
-- Name: ticket_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ticket_id_seq OWNED BY public.ticket.id;


--
-- Name: aircraft id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aircraft ALTER COLUMN id SET DEFAULT nextval('public.aircraft_id_seq'::regclass);


--
-- Name: flight id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight ALTER COLUMN id SET DEFAULT nextval('public.flight_id_seq'::regclass);


--
-- Name: ticket id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket ALTER COLUMN id SET DEFAULT nextval('public.ticket_id_seq'::regclass);


--
-- Data for Name: aircraft; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.aircraft (id, model) FROM stdin;
1	Боинг 777-300
2	Боинг 737-300
3	Аэробус A320-200
4	Суперджет-100
\.


--
-- Data for Name: airport; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.airport (code, country, city) FROM stdin;
MNK	Беларусь	Минск
LDN	Англия	Лондон
MSK	Россия	Москва
BSL	Испания	Барселона
\.


--
-- Data for Name: flight; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flight (id, flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status) FROM stdin;
1	MN3002	2020-06-14 14:30:00	MNK	2020-06-14 18:07:00	LDN	1	ARRIVED
2	MN3002	2020-06-16 09:15:00	LDN	2020-06-16 13:00:00	MNK	1	ARRIVED
3	BC2801	2020-07-28 23:25:00	MNK	2020-07-29 02:43:00	LDN	2	ARRIVED
4	BC2801	2020-08-01 11:00:00	LDN	2020-08-01 14:15:00	MNK	2	DEPARTED
5	TR3103	2020-05-03 13:10:00	MSK	2020-05-03 18:38:00	BSL	3	ARRIVED
6	TR3103	2020-05-10 07:15:00	BSL	2020-05-10 12:44:00	MSK	3	CANCELLED
7	CV9827	2020-09-09 18:00:00	MNK	2020-09-09 19:15:00	MSK	4	SCHEDULED
8	CV9827	2020-09-19 08:55:00	MSK	2020-09-19 10:05:00	MNK	4	SCHEDULED
9	QS8712	2020-12-18 03:35:00	MNK	2020-12-18 06:46:00	LDN	2	ARRIVED
\.


--
-- Data for Name: seat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seat (aircraft_id, seat_no) FROM stdin;
1	A1
1	A2
1	B1
1	B2
1	C1
1	C2
1	D1
1	D2
2	A1
2	A2
2	B1
2	B2
2	C1
2	C2
2	D1
2	D2
3	A1
3	A2
3	B1
3	B2
3	C1
3	C2
3	D1
3	D2
4	A1
4	A2
4	B1
4	B2
4	C1
4	C2
4	D1
4	D2
\.


--
-- Data for Name: ticket; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ticket (id, passenger_no, passenger_name, flight_id, seat_no, cost) FROM stdin;
1	112233	Иван Иванов	1	A1	200.00
2	23234A	Петр Петров	1	B1	180.00
3	SS988D	Светлана Светикова	1	B2	175.00
4	QYASDE	Андрей Андреев	1	C2	175.00
5	POQ234	Иван Кожемякин	1	D1	160.00
6	898123	Олег Рубцов	1	A2	198.00
7	555321	Екатерина Петренко	2	A1	250.00
8	QO23OO	Иван Розмаринов	2	B2	225.00
9	9883IO	Иван Кожемякин	2	C1	217.00
10	123UI2	Андрей Буйнов	2	C2	227.00
11	SS988D	Светлана Светикова	2	D2	277.00
12	EE2344	Дмитрий Трусцов	3	А1	300.00
13	AS23PP	Максим Комсомольцев	3	А2	285.00
14	322349	Эдуард Щеглов	3	B1	99.00
15	DL123S	Игорь Беркутов	3	B2	199.00
16	MVM111	Алексей Щербин	3	C1	299.00
17	ZZZ111	Денис Колобков	3	C2	230.00
18	234444	Иван Старовойтов	3	D1	180.00
19	LLLL12	Людмила Старовойтова	3	D2	224.00
20	RT34TR	Степан Дор	4	A1	129.00
21	999666	Анастасия Шепелева	4	A2	152.00
22	234444	Иван Старовойтов	4	B1	140.00
23	LLLL12	Людмила Старовойтова	4	B2	140.00
24	LLLL12	Роман Дронов	4	D2	109.00
25	112233	Иван Иванов	5	С2	170.00
26	NMNBV2	Лариса Тельникова	5	С1	185.00
27	DSA586	Лариса Привольная	5	A1	204.00
28	DSA583	Артур Мирный	5	B1	189.00
29	DSA581	Евгений Кудрявцев	6	A1	204.00
30	EE2344	Дмитрий Трусцов	6	A2	214.00
31	AS23PP	Максим Комсомольцев	6	B2	176.00
32	112233	Иван Иванов	6	B1	135.00
33	309623	Татьяна Крот	6	С1	155.00
34	319623	Юрий Дувинков	6	D1	125.00
35	322349	Эдуард Щеглов	7	A1	69.00
36	DIOPSL	Евгений Безфамильная	7	A2	58.00
37	DIOPS1	Константин Швец	7	D1	65.00
38	DIOPS2	Юлия Швец	7	D2	65.00
39	1IOPS2	Ник Говриленко	7	C2	73.00
40	999666	Анастасия Шепелева	7	B1	66.00
41	23234A	Петр Петров	7	C1	80.00
42	QYASDE	Андрей Андреев	8	A1	100.00
43	1QAZD2	Лариса Потемнкина	8	A2	89.00
44	5QAZD2	Карл Хмелев	8	B2	79.00
45	2QAZD2	Жанна Хмелева	8	С2	77.00
46	BMXND1	Светлана Хмурая	8	В2	94.00
47	BMXND2	Кирилл Сарычев	8	D1	81.00
48	SS988D	Светлана Светикова	9	A2	222.00
49	SS978D	Андрей Желудь	9	A1	198.00
50	SS968D	Дмитрий Воснецов	9	B1	243.00
52	112233	Иван Иванов	9	С2	135.00
53	NMNBV2	Лариса Тельникова	9	B2	217.00
54	23234A	Петр Петров	9	D1	189.00
55	123951	Полина Зверева	9	D2	234.00
51	SS958D	Александр Расторгуев	9	С1	543.20
\.


--
-- Name: aircraft_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.aircraft_id_seq', 4, true);


--
-- Name: flight_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.flight_id_seq', 9, true);


--
-- Name: ticket_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ticket_id_seq', 58, true);


--
-- Name: aircraft aircraft_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aircraft
    ADD CONSTRAINT aircraft_pkey PRIMARY KEY (id);


--
-- Name: airport airport_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airport
    ADD CONSTRAINT airport_pkey PRIMARY KEY (code);


--
-- Name: flight flight_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_pkey PRIMARY KEY (id);


--
-- Name: seat seat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seat
    ADD CONSTRAINT seat_pkey PRIMARY KEY (aircraft_id, seat_no);


--
-- Name: ticket ticket_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (id);


--
-- Name: unique_flight_id_seat_no_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX unique_flight_id_seat_no_idx ON public.ticket USING btree (flight_id, seat_no);


--
-- Name: flight flight_aircraft_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_aircraft_id_fkey FOREIGN KEY (aircraft_id) REFERENCES public.aircraft(id);


--
-- Name: flight flight_arrival_airport_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_arrival_airport_code_fkey FOREIGN KEY (arrival_airport_code) REFERENCES public.airport(code);


--
-- Name: flight flight_departure_airport_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_departure_airport_code_fkey FOREIGN KEY (departure_airport_code) REFERENCES public.airport(code);


--
-- Name: seat seat_aircraft_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seat
    ADD CONSTRAINT seat_aircraft_id_fkey FOREIGN KEY (aircraft_id) REFERENCES public.aircraft(id);


--
-- Name: ticket ticket_flight_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_flight_id_fkey FOREIGN KEY (flight_id) REFERENCES public.flight(id);


--
-- PostgreSQL database dump complete
--

