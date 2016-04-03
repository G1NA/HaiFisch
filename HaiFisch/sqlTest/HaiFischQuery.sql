use foursquare;

SELECT id, POI, POI_name, photos, COUNT(POI) as numOfPOIs,
	 COUNT(DISTINCT(CASE photos WHEN 'Not exists' THEN NULL ELSE photos END)) as NumOfDisPhotos
FROM checkins
WHERE latitude > 40 AND latitude < 41
	AND longitude > -74.25 AND longitude < -73.7
    AND `time` > '2012-04-03 20:58:00' and `time` < '2012-05-01 21:00:00'
GROUP BY POI
ORDER BY numOfPOIs DESC
LIMIT 100