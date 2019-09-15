drop table if exists dev_lake_jo.jo_top5_male_female_last_games;

create table if not exists dev_lake_jo.jo_top5_male_female_last_games
as

select ttt.ID as ID,
  ttt.Name as Name,
  ttt.sum_medal as Medals,
  ttt.r as the_Rank
from (
    select tt.ID, tt.Name, 
           tt.sum_medal,
           row_number() over(order by tt.sum_medal desc) as r
    from (
        select t.ID, t.Name,
                sum(t.medal) as sum_medal
        from (
            select ID, Name,
                    case Medal
                       when 'Gold' then 1000000
                       when 'Silver' then 1000
                       when 'Bronze' then 1
                       else 0
                    end as medal
            from dev_lake_jo.jo_full_games g
            where g.Games in (select max(gg.Games) from dev_lake_jo.jo_full_games gg)
                and Medal != 'NA'
        ) t
        group by t.ID, t.Name
    ) tt
)ttt
where ttt.r <= 5;
