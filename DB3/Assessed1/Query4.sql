--SELECT
--FROM Band as b, Release as r, (SELECT count(Song.title) as SongCount, 
--				FROM Song, Release, 
--				WHERE Release.rid == Song.rid AND 



--;
--SELECT MAX(song.title) as song_count, Release.rid as release_id
--FROM Song, Release
--WHERE Release.rid = Song.rid
--GROUP BY Release.rid
--ORDER BY song_count DESC;

SELECT max(sumSongs.songCount)
FROM (SELECT COUNT(title) AS songCount
FROM Song
GROUP BY title) as sumSongs;
