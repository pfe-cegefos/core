drop table if exists dev_lake_jo.jo_top5_sport_per_team;

create table if not exists dev_lake_jo.jo_top5_sport_per_team
as

select * from (
    select tt.Team, tt.Sport, tt.sum_medal, row_number() over (partition by tt.Team order by tt.sum_medal desc) as rank
    from (
        select t.Team,
                t.Sport,
                sum(t.medal) as sum_medal
        from (
            select Team,
                    Sport,
                    case Medal
                        when 'Gold' then 1000000
                        when 'Silver' then 1000
                        when 'Bronze' then 1
                        else 0
                    end as medal
            from dev_lake_jo.jo_full_games
            where Medal != 'NA'
        ) t
        group by t.Team, t.Sport
    )tt
)ttt
where rank <= 5;
