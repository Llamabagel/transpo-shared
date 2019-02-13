-- Create stops
create table stops
(
  id                 varchar(10)      not null
    constraint stops_pk
      primary key,
  code               varchar(10),
  name               text             not null,
  description        text,
  latitude           double precision not null,
  longitude          double precision not null,
  zoneId             integer,
  stopUrl            text,
  locationType       integer,
  parentStation      varchar(10),
  timezone           varchar(10),
  wheelchairBoarding integer
);

create unique index stops_id_uindex
  on stops (id);

-- Create routes
create table routes
(
  id          varchar(10) not null
    constraint routes_pk
      primary key,
  agencyId    text,
  shortName   text        not null,
  longName    text        not null,
  description text,
  type        integer     not null,
  url         text,
  color       text,
  textColor   text,
  sortOrder   integer
);

create unique index routes_id_uindex
  on routes (id);

-- Create agencies
create table agencies
(
  id       text not null
    constraint agencies_pk
      primary key,
  name     text not null,
  url      text not null,
  timezone text not null,
  language text,
  phone    text,
  fareUrl  text,
  email    text
);

-- Create calendarDates
create table calendarDates
(
  serviceId     text    not null,
  date          text    not null,
  exceptionType integer not null
);

-- Create calendars
create table calendars
(
  serviceId text    not null
    constraint calendars_pk
      primary key,
  monday    integer not null,
  tuesday   integer not null,
  wednesday integer not null,
  thursday  integer not null,
  friday    integer not null,
  saturday  integer not null,
  sunday    integer not null,
  startDate text    not null,
  endDate   text    not null
);

-- Create stopTimes
create table stopTimes
(
  tripId                text        not null,
  arrivalTime           varchar(15) not null,
  departureTime         varchar(15) not null,
  stopId                varchar(10) not null,
  stopSequence          integer     not null,
  stopHeadsign          text,
  pickupType            integer,
  dropOffType           integer,
  shapeDistanceTraveled double precision,
  timepoint             integer
);

-- Create trips
create table trips
(
  routeId              text not null,
  serviceId            text not null,
  tripId               text not null
    constraint trips_pk
      primary key,
  headsign             text,
  shortName            text,
  directionId          integer,
  blockId              text,
  shapeId              text,
  wheelchairAccessible integer,
  bikesAllowed         integer
);
