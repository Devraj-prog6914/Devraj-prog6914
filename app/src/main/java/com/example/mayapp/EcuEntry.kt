data class EcuEntry(
    val vehicle_speed_kmh: Int = 0,
    val fuel_pressure_bar: Double = 0.0,
    val air_intake_temperature_c: Int = 0,
    val fuel_trim_percent: Int = 0,
    val oil_pressure_bar: Double = 0.0,
    val transmission_gear: Int = 0,
    val battery_voltage_v: Double = 0.0
)