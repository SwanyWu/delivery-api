CREATE TABLE deliveries (
    id UUID PRIMARY KEY,
    vehicleId VARCHAR(255),
    startedAt TIMESTAMP,
    finishedAt TIMESTAMP,
    status VARCHAR(50)
);
