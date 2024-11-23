CREATE TABLE deliveries (
    id UUID PRIMARY KEY, -- indexed by default
    vehicleId VARCHAR(255),
    startedAt TIMESTAMP WITH TIME ZONE,
    finishedAt TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50)
);
