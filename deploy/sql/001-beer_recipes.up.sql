CREATE TABLE beer_recipes (
  recipe_id uuid NOT NULL,
  created_at timestamptz NOT NULL,
  recipe jsonb NOT NULL,
  generator_type varchar NOT NULL,
  metadata jsonb NULL,
  CONSTRAINT beer_recipes_pkey PRIMARY KEY (recipe_id)
);

-- CREATE INDEX beer_recipes_created_idx ON beer_recipes USING btree (created_at);
