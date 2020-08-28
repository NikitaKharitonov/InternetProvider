SET timezone = '+04';


CREATE TYPE status AS ENUM ('PLANNED', 'ACTIVE', 'SUSPENDED', 'DISCONNECTED');
CREATE TYPE connection_type AS ENUM ('ADSL', 'Dial_up', 'ISDN', 'Cable', 'Fiber');

CREATE TABLE client (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        phone_number VARCHAR(12) NOT NULL,
                        email_address VARCHAR(129) NOT NULL
);

CREATE TABLE temporalInternet (
                          id SERIAL PRIMARY KEY,
                          client_id INT NOT NULL REFERENCES client(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          activation_date TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
                          status status NOT NULL
);

CREATE TABLE temporalPhone (
                       id SERIAL PRIMARY KEY,
                       client_id INT NOT NULL REFERENCES client(id) ON DELETE CASCADE ON UPDATE CASCADE,
                       activation_date TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
                       status status NOT NULL
);

CREATE TABLE temporalTelevision (
                            id SERIAL PRIMARY KEY,
                            client_id INT NOT NULL REFERENCES client(id) ON DELETE CASCADE ON UPDATE CASCADE,
                            activation_date TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
                            status status NOT NULL
);

CREATE TABLE internet_history (
                                  id SERIAL PRIMARY KEY,
                                  internet_id INT NOT NULL REFERENCES temporalInternet(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                  begin_date TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
                                  end_date TIMESTAMP(0) WITHOUT TIME ZONE,
                                  speed INT NOT NULL,
                                  antivirus BOOLEAN,
                                  connection_type connection_type NOT NULL
);

CREATE TABLE phone_history (
                               id SERIAL PRIMARY KEY,
                               phone_id INT NOT NULL REFERENCES temporalPhone(id) ON DELETE CASCADE ON UPDATE CASCADE,
                               begin_date TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
                               end_date TIMESTAMP(0) WITHOUT TIME ZONE,
                               mins_count INT NOT NULL,
                               sms_count INT NOT NULL
);

CREATE TABLE television_history (
                                    id SERIAL PRIMARY KEY,
                                    television_id INT NOT NULL REFERENCES temporalTelevision(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                    begin_date TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
                                    end_date TIMESTAMP(0) WITHOUT TIME ZONE,
                                    channels_count INT NOT NULL
);








