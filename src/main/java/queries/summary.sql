select sp.symbol , sp.name, rd.open open, rd.high, rd.low, rd.close close, rd.volume,
CASE
    WHEN rd.open < rd.close THEN 100 - open/close*100
    WHEN rd.open > rd.close THEN -1 * (100 - close/open*100)
    ELSE 0
END daily_change_percent,
CASE
    WHEN rd.open = rd.close THEN 0
    ELSE open-close
END daily_change_value,
rd.date
from sp_500 sp 
join raw_data_sp_500_daily rd on sp.symbol = rd.symbol
where open > 0 and close > 0 and date = '2020-05-13' 
order by daily_change_percent desc