-- EXPLAIN: 
--
-- "Sort  (cost=270.98..271.04 rows=24 width=51)"
-- "  Sort Key: (count(song.cdbonus))"
-- "  ->  Hash Join  (cost=254.00..270.43 rows=24 width=51)"
-- "        Hash Cond: (b.bid = band.bid)"
-- "        ->  Seq Scan on band b  (cost=0.00..14.50 rows=450 width=47)"
-- "        ->  Hash  (cost=253.70..253.70 rows=24 width=12)"
-- "              ->  HashAggregate  (cost=253.22..253.46 rows=24 width=6)"
-- "                    ->  Nested Loop  (cost=30.46..253.10 rows=24 width=6)"
-- "                          ->  Hash Join  (cost=30.46..245.17 rows=24 width=6)"
-- "                                Hash Cond: (song.rid = release.rid)"
-- "                                ->  Seq Scan on song  (cost=0.00..214.38 rows=24 width=6)"
-- "                                      Filter: (cdbonus = 'Y'::bpchar)"
-- "                                ->  Hash  (cost=18.54..18.54 rows=954 width=8)"
-- "                                      ->  Seq Scan on release  (cost=0.00..18.54 rows=954 width=8)"
-- "                          ->  Index Only Scan using band_pkey on band  (cost=0.00..0.32 rows=1 width=4)"
-- "                                Index Cond: (bid = release.bid)"


SELECT b.name AS Band_Name, cds.count AS BONUS_NUMBER
FROM Band AS b, (SELECT count(cdbonus) as count, Band.bid as band_bid
		FROM Band, Release, Song
		WHERE Song.cdbonus = 'Y' AND Band.bid = Release.bid AND Release.rid = Song.rid
		GROUP BY Band.bid) as cds
WHERE b.bid = cds.band_bid
ORDER BY cds.count DESC