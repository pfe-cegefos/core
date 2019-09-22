drop table if exists dev_lake_jo.jo_avg_age_country_over_5_last_years;

create table if not exists dev_lake_jo.jo_avg_age_country_over_5_last_years
as
select Team,
       Year,
       avg(Age) as avg_age
from dev_lake_jo.jo_full_games
where substr(Games, 1, 4) > substr(from_unixtime(unix_timestamp()), 1, 4) - 5
group by Team, Year;
