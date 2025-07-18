-- PostgreSQL cleaned script for xmlparser database

SET default_transaction_read_only = off;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

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

-- Создание таблиц

CREATE TABLE public.d_cat_catalog (
    id integer NOT NULL,
    delivery_date timestamp without time zone,
    company character varying(2000),
    uuid character varying(2000) NOT NULL
);

CREATE SEQUENCE public.d_cat_catalog_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.d_cat_catalog_id_seq OWNED BY public.d_cat_catalog.id;

CREATE TABLE public.f_cat_plants (
    common character varying(2000),
    botanical character varying(2000),
    zone integer,
    light character varying(2000),
    price numeric,
    availability integer,
    catalog_id integer
);

-- Значения по умолчанию

ALTER TABLE ONLY public.d_cat_catalog ALTER COLUMN id SET DEFAULT nextval('public.d_cat_catalog_id_seq'::regclass);

COPY public.d_cat_catalog (id, delivery_date, company, uuid) FROM stdin;
\.

COPY public.f_cat_plants (common, botanical, zone, light, price, availability, catalog_id) FROM stdin;
\.

-- Последнее значение последовательности

SELECT pg_catalog.setval('public.d_cat_catalog_id_seq', 302, true);

-- Первичный ключи и связи

ALTER TABLE ONLY public.d_cat_catalog
    ADD CONSTRAINT d_cat_catalog_pkey PRIMARY KEY (id);

CREATE UNIQUE INDEX d_cat_catalog_id_uindex ON public.d_cat_catalog USING btree (id);

ALTER TABLE ONLY public.f_cat_plants
    ADD CONSTRAINT f_cat_plants_d_cat_catalog_id_fk FOREIGN KEY (catalog_id) REFERENCES public.d_cat_catalog(id);
