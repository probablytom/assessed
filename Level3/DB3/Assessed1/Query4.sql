-- This almost definitely works. Finds everything needed, but uncertain that it's finding the albums with the _most_ songs...
select b.name as Band_Name, r.title as Album_Title, cs.countsongs as Song_Num
from band as b inner join release as r on b.bid = r.rid INNER JOIN (Select count(DISTINCT Song.title) as countsongs, release.rid as release_id
							FROM Song, Release
							WHERE Song.rid = Release.rid
							GROUP BY release_id) as cs on r.rid = release_id, Memberof
where Memberof.bid = b.bid and Memberof.instrument like 'vocals'
group by b.name, r.title, cs.countsongs
order by Band_Name ASC 