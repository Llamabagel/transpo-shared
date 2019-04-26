-- Add stop data
insert into stops (id, code, name, latitude, longitude) values ('NF210', '7467', 'ADMIRAL / ANNA', -75.72909, 45.382643);
insert into stops (id, code, name, latitude, longitude) values ('NF220', '7465', 'ADMIRAL / ANNA', -75.729458, 45.382817);
insert into stops (id, code, name, latitude, longitude) values ('AF970', '3030', 'LYCÉE CLAUDEL 2A', -75.664225, 45.406614);
insert into stops (id, code, name, latitude, longitude) values ('AF975', '3031', 'SMYTH 1A', -75.666742, 45.40118);
insert into stops (id, code, name, latitude, longitude) values ('AF980', '3031', 'SMYTH 2A', -75.666591, 45.401167);

-- Add route data
insert into routes (id, short_name, long_name, type) values ('2-288', '2', '', 2);
insert into routes (id, short_name, long_name, type) values ('5-288', '5', '', 3);
insert into routes (id, short_name, long_name, type) values ('291-288', '291', '', 3);

-- Add agency data
insert into agencies (id, name, url, time_zone) values (1, 'OC Transpo', 'http://www.octranspo.com', 'America/Montreal');

-- Add calendarDate data
insert into calendar_dates values ('JAN19-d1930LoR-Weekday-01', '20190212', 2);
insert into calendar_dates values ('JAN19-d1900Sup-Weekday-01', '20190215', 2);
insert into calendar_dates values ('JAN19-JANDA19-Weekday-26', '20190218', 2);

-- Add calendar data
insert into calendars values ('JAN19-JANDA19-Weekday-26', 1, 1, 1, 1, 1, 0, 0, '20190204', '20190418');
insert into calendars values ('JAN19-JANSA19-Saturday-02', 0, 0, 0, 0, 0, 1, 0, '20190209','20190420');
insert into calendars values ('JAN19-JANSU19-Sunday-02', 0, 0, 0, 0, 0, 0, 1, '20190210','20190419');
insert into calendars values ('JAN19-Reduced-Weekday-02', 1, 1, 1, 1, 1, 0, 0, '20190218','20190315');

-- Add stopTime data
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('56994291-JAN19-301Shop-Weekday-01', '08:49:00', '08:49:00', 'WR285', 1, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('56994291-JAN19-301Shop-Weekday-01', '08:49:00', '08:49:00', 'WR295', 2, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('56994291-JAN19-301Shop-Weekday-01', '08:50:00', '08:50:00', 'WR245', 3, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('56994291-JAN19-301Shop-Weekday-01', '08:50:00', '08:50:00', 'WR249', 4, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('56994291-JAN19-301Shop-Weekday-01', '08:50:00', '08:50:00', 'WR255', 5, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('59528499-JAN19-Reduced-Weekday-02', '15:19:00', '15:19:00', 'CK105', 1, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('59528499-JAN19-Reduced-Weekday-02', '15:19:00', '15:19:00', 'CK110', 2, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('59508566-JAN19-JANSA19-Saturday-02', '12:51:00', '12:51:00', 'CK110', 2, 0, 0);
insert into stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, pickup_type, drop_off_type) values ('59508566-JAN19-JANSA19-Saturday-02', '12:50:00', '12:50:00', 'CK105', 1, 0, 0);

-- Add trips data
insert into trips (route_id, service_id, trip_id, headsign, direction_id, block_id) values ('44-288', 'JAN19-JANDA19-Weekday-26', '57328738-JAN19-JANDA19-Weekday-26', 'Gatineau', 1, '6646699');
insert into trips (route_id, service_id, trip_id, headsign, direction_id, block_id) values ('91-288', 'JAN19-JANDA19-Weekday-26', '57328740-JAN19-JANDA19-Weekday-26', 'Baseline', 1, '6647252');
insert into trips (route_id, service_id, trip_id, headsign, direction_id, block_id) values ('91-288', 'JAN19-JANDA19-Weekday-26', '57328743-JAN19-JANDA19-Weekday-26', 'Orléans', 0, '6647242');
insert into trips (route_id, service_id, trip_id, headsign, direction_id, block_id) values ('94-288', 'JAN19-JANDA19-Weekday-26', '57328746-JAN19-JANDA19-Weekday-26', 'Millennium', 1, '6647213');

-- Add shapes data
insert into shapes values ('A_shp', 45.1, -76.1, 0, null);
insert into shapes values ('A_shp', 45.2, -76.2, 1, null);
insert into shapes values ('A_shp', 45.3, -76.3, 2, null);
insert into shapes values ('B_shp', 10.1, 11.1, 15, null);