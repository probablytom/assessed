SELECT Member.name as Guitarist_Name, Band.name as Band_Name, COALESCE(Memberof.startyear, 1901) as Start_Year, COALESCE(Memberof.endyear, 1901) as End_Year
FROM Member, Memberof, Band, Release
WHERE Member.mid = Memberof.mid AND Band.bid = Memberof.bid AND Memberof.instrument = 'guitar'
GROUP BY Guitarist_Name, Band_Name, Start_Year, End_Year
ORDER BY GUITARIST_NAME ASC, Start_Year DESC;

-- Can't use COALESCE!

---------

