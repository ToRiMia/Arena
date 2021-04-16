CREATE TABLE public.battle_progress (
	id bigserial NOT NULL,
	battle_id int8 NOT NULL,
	attacker_id int8 NOT NULL,
	defender_id int8 NOT NULL,
	damage int4 NOT NULL,
	residual_health int4 NOT NULL,
	CONSTRAINT battle_progress_pk PRIMARY KEY (id)
);