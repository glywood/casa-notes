-- Hours were originally stored as a double including fractional hours,
-- but this has been changed to an ISO-8601 period format.
-- e.g. 2.25 hours becomes 'PT2H15M'

ALTER TABLE activity ADD COLUMN duration VARCHAR(50);

UPDATE activity SET duration = 'PT' || CAST(ROUND(hours) AS INT) || 'H' || CAST(ROUND((hours - ROUND(hours)) * 60.0) AS INT) || 'M';

ALTER TABLE activity DROP COLUMN hours;
