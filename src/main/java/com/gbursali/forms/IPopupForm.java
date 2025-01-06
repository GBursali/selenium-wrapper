package com.gbursali.forms;

public interface IPopupForm<T> {
    /**
     * Opens the popup form and performs necessary operations to ensure
     * <p>
     * the popup is ready for user interaction.
     *
     * @return An instance of the current popup form.
     */

    T openPopup();

    /**
     * Saves the popup form. This method should be used after all necessary changes are made to the form.
     */

    void save();

    /**
     * Cancels the popup form. This method should be used to discard any changes
     * <p>
     * made to the form and close the popup without saving.
     */

    void cancel();
}
