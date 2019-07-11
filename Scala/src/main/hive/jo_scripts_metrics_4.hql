drop table if exists dev_app_jo.jo_top5_countries_per_year;

create table if not exists dev_app_jo.jo_top5_countries_per_year
as

select * from (
    select tt.Year, tt.Team, tt.sum_medal, row_number() over (partition by tt.Year order by tt.sum_medal desc) as rank
    from (
        select t.Year,
                t.Team,
                sum(t.medal) as sum_medal
        from (
            select Year,
                    Team,
                    case Medal
                        when 'Gold' then 1000000
                        when 'Silver' then 1000
                        when 'Bronze' then 1
                        else 0
                    end as medal
            from dev_lake_jo.jo_full_games
            where Medal != 'NA'
        ) t
        group by t.Year, t.Team
    )tt
)ttt
where rank <= 5;
