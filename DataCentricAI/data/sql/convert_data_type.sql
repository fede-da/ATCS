-- Modify column avg_area_house_age to DOUBLE PRECISION type
ALTER TABLE "USA_HOUSING"
ALTER COLUMN avg_area_house_age TYPE DOUBLE PRECISION
USING avg_area_house_age ::DOUBLE PRECISION;

-- Modify column avg_area_income to DOUBLE PRECISION type
ALTER TABLE "USA_HOUSING"
ALTER COLUMN avg_area_income TYPE DOUBLE PRECISION
USING avg_area_income::DOUBLE PRECISION;

-- Modify column avg_area_number_of_rooms to DOUBLE PRECISION type
ALTER TABLE "USA_HOUSING"
ALTER COLUMN avg_area_number_of_rooms TYPE DOUBLE PRECISION
USING avg_area_number_of_rooms::DOUBLE PRECISION;

-- Modify column area_number_of_bedrooms to DOUBLE PRECISION type
ALTER TABLE "USA_HOUSING"
ALTER COLUMN area_number_of_bedrooms TYPE DOUBLE PRECISION
USING avg_area_number_of_bedrooms::DOUBLE PRECISION;

-- Modify column area_population to DOUBLE PRECISION type
ALTER TABLE "USA_HOUSING"
ALTER COLUMN area_population TYPE DOUBLE PRECISION
USING area_population ::DOUBLE PRECISION;

-- Modify column price to DOUBLE PRECISION typ
ALTER TABLE "USA_HOUSING"
ALTER COLUMN price TYPE DOUBLE PRECISION
USING price ::DOUBLE PRECISION;