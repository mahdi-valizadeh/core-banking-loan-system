package ir.bank.loan.product.domain;

/**
 * Return model category of an Islamic finance contract.
 */
public enum ContractCategory {
    /** Predetermined profit (e.g. Murabaha). */
    FIXED_RETURN,
    /** Provisional profit + final settlement (e.g. Mudarabah). */
    VARIABLE_RETURN,
    /** No profit; fee only (Qard al-Hasan). */
    NO_RETURN
}
