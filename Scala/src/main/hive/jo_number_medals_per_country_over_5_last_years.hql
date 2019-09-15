drop table if exists dev_lake_jo.jo_number_medals_per_country_over_5_last_year;

create table if not exists dev_lake_jo.jo_number_medals_per_country_over_5_last_year
as

select Team,
       Year,
       count(Medal) as number_medal
from dev_lake_jo.jo_full_games
where Medal != 'NA'
  and substr(Games, 1, 4) > substr(from_unixtime(unix_timestamp()), 1, 4) - 5
group by Team, Year;
