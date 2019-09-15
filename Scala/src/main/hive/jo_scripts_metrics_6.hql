drop table if exists dev_lake_jo.jo_average_age_per_year;

create table if not exists dev_lake_jo.jo_average_age_per_year
as

select Year,
       avg(age) as avg_age
from dev_lake_jo.jo_full_games
group by Year;
