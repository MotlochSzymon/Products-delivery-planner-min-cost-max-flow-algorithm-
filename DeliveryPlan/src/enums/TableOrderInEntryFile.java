package enums;

public enum TableOrderInEntryFile {
        VACCINE_PRODUCERS_TABLE(1),
        PHARMACIES_TABLE(2),
        PRODUCER_PHARMACY_TABLE(3);

        private final int value;

        TableOrderInEntryFile(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
}
