BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `patients` (
                                         id            INTEGER
                                             constraint patients_pk
                                                 primary key autoincrement,
                                         name          TEXT,
                                         surname       TEXT,
                                         phone_number  TEXT,
                                         living_place  TEXT,
                                         diagnosis     TEXT,
                                         date_of_birth DATE,
                                         mass          DOUBLE,
                                         height        DOUBLE
);
INSERT INTO `patients` VALUES (1,'John','Wick','061/123-123', 'California', 'Too cool', -1457744400000, 77.2,187.6);
INSERT INTO `patients` VALUES (2,'Thomas','Edison','061/111-111', 'New York', 'Diagnosis 1', -3861565200000, 85.9,182.5);
INSERT INTO `patients` VALUES (3,'Nikola','Tesla','061/222-123', 'New York', 'Diagnosis 2', -3861738000000, 70.1,173.6);
COMMIT;
