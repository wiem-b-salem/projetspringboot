-- Sample Tours Data for Tuniway Database
-- Run this script in MySQL to populate your database with sample tours
-- Note: Make sure you have guides (users with role GUIDE) in your database first
-- You can create a guide with type='GUIDE' in the user table

-- Insert sample tours
-- These tours reference guide_id=1 (you may need to adjust this based on your actual guide IDs)

INSERT INTO tour (guide_id, name, description, price, date, starting_hour, ending_hour, latitude, longitude) VALUES
(1, 'Medina of Tunis Historical Walking Tour', 'Explore the ancient medina with its historic souks, mosques, and traditional architecture. Learn about Tunisian culture and history from a local guide.', 45.00, '2026-01-20', '09:00:00', '13:00:00', 36.7969, 10.1689),
(1, 'Sidi Bou Said Sunset Tour', 'Experience the beautiful blue and white architecture of Sidi Bou Said with a guided tour ending at sunset with sea views and dinner.', 55.00, '2026-01-25', '15:00:00', '19:00:00', 36.8684, 10.3572),
(1, 'Carthage Ancient Ruins Full Day Tour', 'Full day archaeological tour of Carthage ruins including the Roman villas, amphitheatre, and museum. Includes lunch at a local restaurant.', 75.00, '2026-02-01', '08:00:00', '17:00:00', 36.8536, 10.3276),
(1, 'Bardo Museum Guided Tour', 'Expert-guided tour of the National Bardo Museum with focus on ancient Roman mosaics and artifacts. Perfect for art and history enthusiasts.', 35.00, '2026-01-22', '10:00:00', '13:00:00', 36.8083, 10.1891),
(1, 'Dougga Roman City Day Excursion', 'Visit one of the best-preserved Roman towns in North Africa. Includes guided tour of temples, theatre, and residential areas.', 65.00, '2026-02-05', '08:30:00', '16:00:00', 36.4188, 9.2158),
(1, 'Kairouan Holy City Pilgrimage Tour', 'Sacred journey to one of Islam\'s holiest cities. Visit the Great Mosque, mausoleums, and learn about Islamic heritage.', 50.00, '2026-02-10', '09:00:00', '14:00:00', 35.6761, 10.0965),
(1, 'La Marsa Beach & Water Sports Day', 'Beach day with water sports activities including jet skiing, parasailing, and beach volleyball. Includes lunch and refreshments.', 60.00, '2026-01-28', '10:00:00', '17:00:00', 36.8606, 10.3135),
(1, 'Sfax Medina Cultural Immersion', 'Deep dive into the authentic medina of Sfax with visits to traditional workshops, leather tanneries, and artisan shops.', 40.00, '2026-02-02', '09:00:00', '13:00:00', 34.7406, 10.7603),
(1, 'Hammamet Beach & Medina Combo Tour', 'Combine beach relaxation with cultural exploration of Hammamet\'s old medina. Perfect for families.', 45.00, '2026-01-30', '10:00:00', '16:00:00', 36.4023, 10.6158),
(1, 'Chenini Berber Village Trek', 'Hiking tour to the traditional hilltop Berber village with cave dwellings. Experience authentic village life and traditional cuisine.', 70.00, '2026-02-08', '07:00:00', '17:00:00', 33.7656, 9.8067),
(1, 'Jerba Island Discovery Tour', 'Multi-day tour of Jerba island including beaches, traditional villages, synagogue, and local markets.', 150.00, '2026-02-12', '08:00:00', '18:00:00', 33.8245, 10.9506),
(1, 'El Djem Amphitheatre Evening Tour', 'Visit the impressive UNESCO World Heritage amphitheatre. Evening tour with light show and sunset views.', 40.00, '2026-02-03', '16:00:00', '19:00:00', 35.2984, 10.7319),
(1, 'Ksar Ouled Soltane Historic Fort Tour', 'Explore the ancient fortified granary with traditional architecture. Includes photography stops and cultural explanation.', 35.00, '2026-02-06', '10:00:00', '13:00:00', 33.4733, 9.5583),
(1, 'Tabarka Coastal Adventure', 'Coastal tour combining hiking, snorkeling in coral reefs, and fort exploration. Beach picnic included.', 80.00, '2026-02-14', '08:00:00', '17:00:00', 36.9565, 8.7597),
(1, 'Gafsa Oasis Desert Experience', 'Desert tour through date palm oases with camel riding, traditional lunch, and sunset in the desert.', 85.00, '2026-02-15', '06:00:00', '19:00:00', 34.4269, 8.7839),
(1, 'Matmata Underground Dwellings Tour', 'Unique tour of underground troglodyte homes with local family visits and traditional lunch prepared in underground kitchens.', 55.00, '2026-02-09', '09:00:00', '15:00:00', 33.7433, 9.7483),
(1, 'Sousse Medina & Beach Holiday', 'Relaxing day combining medina exploration with beach time. Includes shopping in traditional souks and dinner by the sea.', 50.00, '2026-02-04', '10:00:00', '18:00:00', 35.8256, 10.6369),
(1, 'Cap Bon Peninsula Scenic Loop', 'Scenic driving tour around Cap Bon peninsula with stops at fishing villages, olive groves, and viewpoints.', 60.00, '2026-02-07', '09:00:00', '17:00:00', 36.9211, 10.6944),
(1, 'Ichkeul Lake Bird Watching Safari', 'Nature tour for bird watching at this important wetland reserve. Best season for migratory birds. Equipment provided.', 45.00, '2026-02-11', '07:00:00', '12:00:00', 37.1597, 9.8933),
(1, 'Tozeur Oasis Desert Festival Tour', 'Experience the Tozeur oasis with traditional architecture tour, camel market visit, and desert film location tour.', 95.00, '2026-02-16', '08:00:00', '18:00:00', 33.9164, 8.1358);

-- Insert place_ids for tours (tour_place_ids table)
-- Each tour visits multiple places
INSERT INTO tour_place_ids (tour_id, place_ids) VALUES
(1, 1), -- Medina of Tunis
(2, 2), -- Sidi Bou Said
(3, 3), -- Carthage Ruins
(3, 4), -- + Bardo Museum
(4, 4), -- Bardo Museum
(5, 5), -- Dougga
(6, 6), -- Kairouan
(7, 7), -- La Marsa Beach
(8, 8), -- Sfax Medina
(9, 9), -- Hammamet
(10, 10), -- Chenini
(11, 11), -- Jerba Island
(12, 12), -- El Djem
(13, 13), -- Ksar Ouled Soltane
(14, 14), -- Tabarka
(15, 15), -- Gafsa Oasis
(16, 16), -- Matmata
(17, 17), -- Sousse
(18, 18), -- Cap Bon
(19, 19), -- Ichkeul Lake
(20, 20); -- Tozeur Oasis

-- Verify the insertion
SELECT COUNT(*) as total_tours FROM tour;
