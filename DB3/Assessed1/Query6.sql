select b.name as Band_Name, m.name as Keyboard_Player
FROM Band AS b LEFT OUTER JOIN Memberof AS mo on mo.bid = b.bid, Member as m
where mo.instrument like 'keyboards' AND m.mid = mo.mid
GROUP BY Band_Name, Keyboard_Player