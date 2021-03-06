CREATE TABLE person (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR NOT NULL
);

CREATE TABLE activity (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  person_id INT NOT NULL,
  type VARCHAR NOT NULL,
  description VARCHAR NOT NULL,
  date DATE NOT NULL,
  hours DOUBLE NOT NULL,
  summary VARCHAR NOT NULL,
  successes VARCHAR NOT NULL,
  concerns VARCHAR NOT NULL,
  selfesteem BOOLEAN NOT NULL,
  trust BOOLEAN NOT NULL,
  cultural BOOLEAN NOT NULL,
  experiences BOOLEAN NOT NULL,
  educational BOOLEAN NOT NULL,
  extracurricular BOOLEAN NOT NULL,
  healthy BOOLEAN NOT NULL,
  milestones BOOLEAN NOT NULL,
  created_at TIMESTAMP NOT NULL,
  modified_at TIMESTAMP NOT NULL
);
