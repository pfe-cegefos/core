drop table if exists dev_lake_jo.jo_number_different_medals_per_country;

create table if not exists dev_lake_jo.jo_number_different_medals_per_country
as

select Team, 
       Medal,
       count(Medal) as number_medal
from dev_lake_jo.jo_full_games
where Medal != 'NA'
group by Team, Medal;
