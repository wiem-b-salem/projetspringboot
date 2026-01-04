-- Sample Places Data for Tuniway Database
-- Run this script in MySQL to populate your database with sample places

INSERT INTO place (name, description, picture_url, category, latitude, longitude, city) VALUES
('Medina of Tunis', 'Historic medina with traditional architecture and souks', 'https://www.kanaga-at.com/wp-content/uploads/2021/07/tunisia_tunisi_foto_i._fornasiero.jpg', 'CULTURAL_SITE', 36.7969, 10.1689, 'Tunis'),
('Sidi Bou Said', 'Picturesque coastal town with blue and white architecture', 'https://images.unsplash.com/photo-1584506252957-71487b3b896e?w=400', 'TOURIST_ATTRACTION', 36.8684, 10.3572, 'Sidi Bou Said'),
('Carthage Ruins', 'Ancient Roman archaeological site with historical ruins', 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400', 'ARCHAEOLOGICAL_SITE', 36.8536, 10.3276, 'Carthage'),
('Bardo Museum', 'National museum with ancient mosaics and artifacts', 'https://images.unsplash.com/photo-1579944390987-30c54f1c3a85?w=400', 'MUSEUM', 36.8083, 10.1891, 'Tunis'),
('Dougga Roman Site', 'Well-preserved Roman ruins on a hilltop', 'https://images.unsplash.com/photo-1562883676-8c6fbfe76c4b?w=400', 'ARCHAEOLOGICAL_SITE', 36.4188, 9.2158, 'Dougga'),
('Kairouan Great Mosque', 'One of the most important mosques in Islam', 'https://images.unsplash.com/photo-1516639415265-5df61e8e52df?w=400', 'RELIGIOUS_SITE', 35.6761, 10.0965, 'Kairouan'),
('La Marsa Beach', 'Beautiful sandy beach near Tunis with water sports', 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400', 'BEACH', 36.8606, 10.3135, 'La Marsa'),
('Sfax Medina', 'Historic medina with traditional Tunisian architecture', 'https://images.unsplash.com/photo-1464037866556-6812c9d1c72e?w=400', 'CULTURAL_SITE', 34.7406, 10.7603, 'Sfax'),
('Hammamet Beach Resort', 'Popular beach destination with resort facilities', 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=400', 'BEACH', 36.4023, 10.6158, 'Hammamet'),
('Chenini Berber Village', 'Traditional hilltop Berber settlement with caves', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400', 'CULTURAL_SITE', 33.7656, 9.8067, 'Tataouine'),
('Jerba Island', 'Island destination with beaches and traditional villages', 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=400', 'BEACH', 33.8245, 10.9506, 'Jerba'),
('El Djem Amphitheatre', 'Ancient Roman amphitheatre, UNESCO World Heritage Site', 'https://images.unsplash.com/photo-1518599504759-b4aa281af52a?w=400', 'ARCHAEOLOGICAL_SITE', 35.2984, 10.7319, 'El Djem'),
('Ksar Ouled Soltane', 'Ancient fortified granary with traditional architecture', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400', 'HISTORICAL_SITE', 33.4733, 9.5583, 'Tataouine'),
('Tabarka Beach and Town', 'Coastal town with beach, coral reefs and fortress', 'https://images.unsplash.com/photo-1502933691298-84fc14542831?w=400', 'BEACH', 36.9565, 8.7597, 'Tabarka'),
('Gafsa Oasis', 'Desert oasis with date palm plantations', 'https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=400', 'NATURAL_SITE', 34.4269, 8.7839, 'Gafsa'),
('Matmata Underground Dwellings', 'Traditional underground troglodyte homes', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400', 'CULTURAL_SITE', 33.7433, 9.7483, 'Matmata'),
('Sousse Medina', 'Beautiful medina with traditional shops and restaurants', 'https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=400', 'CULTURAL_SITE', 35.8256, 10.6369, 'Sousse'),
('Cap Bon Peninsula', 'Scenic coastal area with villages and beaches', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400', 'NATURAL_SITE', 36.9211, 10.6944, 'Cap Bon'),
('Ichkeul Lake National Park', 'Wetland reserve with bird watching opportunities', 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400', 'NATURE_RESERVE', 37.1597, 9.8933, 'Bizerte'),
('Tozeur Oasis', 'Large desert oasis with traditional architecture', 'https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=400', 'TOURIST_ATTRACTION', 33.9164, 8.1358, 'Tozeur');

-- Verify the insertion
SELECT COUNT(*) as total_places FROM place;
