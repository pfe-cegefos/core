drop table if exists dev_lake_jo.jo_number_different_medals_per_athlete;

create table if not exists dev_lake_jo.jo_number_different_medals_per_athlete
as

select ID, Name, 
       Medal,
       count(Medal) number_medal
from dev_lake_jo.jo_full_games
where Medal != 'NA'
group by ID, Name, Medal;
