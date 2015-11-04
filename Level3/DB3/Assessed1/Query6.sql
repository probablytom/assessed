--ALTER TABLE Memberof
--ADD nullcolumn character varying(4) DEFAULT 'NULL';

SELECT b.name as Band_Name, m.name as Keyboard_Player
FROM Band as b, Member as m, Memberof as mo
where mo.instrument like 'keyboards' and mo.bid = b.bid and mo.mid = m.mid
	UNION
SELECT b.name as Band_Name, mo.nullcolumn as Keyboard_Player
FROM Band as b, Member as M, Memberof as mo
WHERE NOT EXISTS (Select m.name WHERE mo.instrument like 'keyboards' and mo.bid = b.bid and mo.mid = m.mid)
ORDER BY Band_Name ASC, Keyboard_Player ASC;

--ALTER TABLE Memberof
--DROP nullcolumn;

-- Second query, using outer joins
select b.name as Band_Name, ISNULL(m.name, "NULL") as Keyboard_Player
FROM Band AS b LEFT OUTER JOIN Memberof AS mo on mo.bid = b.bid, Member as m
where mo.instrument like 'keyboards' AND m.mid = mo.mid
GROUP BY Band_Name, Keyboard_Player;
