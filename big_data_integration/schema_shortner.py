import json

# Load the original mediated schema JSON file
with open('./mediated_schema.json', 'r') as file:
    data = json.load(file)

# Example of semantic grouping
semantic_groups = {
    "Display_Properties": [
        "screen_type", "screen_size_diagonal", "screen_size_horizontal", 
        "screen_size_vertical", "aspect ratio", "backlight technology",
        "brightness", "contrast_ratio_dynamic", "contrast_ratio_static", 
        "displayport_quantity", "minidisplayport_quantity", "display_resolution"
    ],
    "Port_Quantities": [
        "dvi_port_quantity", "usb_port_quantity", "hdmi_port_quantity",
        "ethernet_port_quantity", "displayport_quantity", "minidisplayport_quantity"
    ],
    "Device_Specifications": [
        "color", "color_feet", "monitor_depth", "monitor_depth_with_stand", 
        "monitor_dimension", "monitor_dimension_with_stand", "monitor_height", 
        "monitor_height_with_stand", "monitor_weight", "monitor_weight_with_stand", 
        "monitor_width", "monitor_width_with_stand", "brand", "mount_model", "mount_type"
    ],
    "Power_Consumption": [
        "power_consumption_max", "power_consumption_standy", "power_consumption_class",
        "energy star compliant"
    ],
    "Refresh_Rates": [
        "horizontal_refresh_rate_max", "horizontal_refresh_rate_range",
        "vertical_refresh_rate_max", "vertical_refresh_rate_range",
        "digital_horizontal_refresh_rate_max", "digital_horizontal_refresh_rate_range",
        "digital_vertical_refresh_rate_max", "digital_vertical_refresh_rate_range"
    ],
    "Packaging": [
        "packaging_depth", "packaging_dimension", "packaging_height", 
        "packaging_weight", "packaging_width"
    ],
    "Temperature_and_Humidity": [
        "working_temperature_max", "working_temperature_min", 
        "working_temperature_range", "non_operating_working_altitude",
        "operating_working_altitude", "working_humidity"
    ],
    "Audio_Features": [
        "has_microphone", "has_speakers", "has_headphone_port"
    ]
}

# Create a new dictionary to store aggregated data
consolidated_data = {}

# Aggregate data based on semantic groups
for group_name, keys in semantic_groups.items():
    consolidated_count = sum(data.get(key, 0) for key in keys)
    consolidated_data[group_name] = consolidated_count

# Include keys not covered by the groups
for key in data:
    if not any(key in group for group in semantic_groups.values()):
        consolidated_data[key] = data[key]

# Save the consolidated data back to a new JSON file
with open('./consolidated_mediated_schema.json', 'w') as file:
    json.dump(consolidated_data, file, indent=4)

print("Consolidated schema has been saved to 'consolidated_mediated_schema.json'.")