drop table if exists dev_lake_jo.jo_top5_athletes_per_sport;

create table if not exists dev_lake_jo.jo_top5_athletes_per_sport
as

select * from (
    select tt.Sport,
            tt.ID,
            tt.Name,
            concat(cast(floor(tt.sum_medal / 1000000) as string),' g, ',
                 cast(floor((tt.sum_medal % 1000000) / 1000) as string),' s, ',
                 cast(tt.sum_medal % 1000 as string),' b') as Medals,
            row_number() over (partition by tt.Sport order by tt.sum_medal desc) as rank
    from (
        select t.Sport,
                t.ID, t.Name,
                sum(t.medal) as sum_medal
        from (
            select Sport,
                    ID, Name,
                    case Medal
                        when 'Gold' then 1000000
                        when 'Silver' then 1000
                        when 'Bronze' then 1
                        else 0
                    end as medal
            from dev_lake_jo.jo_full_games
            where Medal != 'NA'
        ) t
        group by t.Sport, t.ID, t.Name
    )tt
)ttt
where rank <= 5;
