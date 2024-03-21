package de.tikalabs.commons.database.servicelayer;

public interface IServiceLayer<T> {

    // Bestehende Methode, wird von bestehenden Projekten genutzt

    /**
     * @deprecated Use  instead.
     */
    @Deprecated
    boolean addRow(T row);

    // Neue überladene Methode, die einen Tabellennamen berücksichtigt
    default boolean insert(String tableName, T row) {
        // Standardimplementierung kann einen UnsupportedOperationException werfen,
        // oder du könntest eine Logik implementieren, die für allgemeine Fälle sinnvoll ist.
        throw new UnsupportedOperationException("Diese Operation wird nicht unterstützt.");
    }
}

