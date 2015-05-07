package com.quailstreetsoftware.newsreader.common;

/**
 * Enums that represent an event to pass along on the event bus.
 * @author jonathan
 *
 */
public enum NotificationEvent {
    CHANGED_SELECTED_SOURCE,
    DISPLAY_ITEM,
    REFRESH_SUBSCRIPTION,
    REFRESH_SUBSCRIPTION_UI,
    DEBUG_MESSAGE, 
    TOGGLE_DEBUG,
    DELETE_SUBSCRIPTION,
    PLAY_SOUND,
    TOGGLE_SOUNDS,
    NEW_SUBSCRIPTION,
    ADD_SUBSCRIPTION_UI,
    DELETE_ARTICLE,
    SUBSCRIPTION_CHANGED
}
