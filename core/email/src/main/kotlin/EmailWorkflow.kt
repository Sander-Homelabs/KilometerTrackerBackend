enum class EmailWorkflow(val knockKey: String) {
    ERROR_NOTIFICATION("error-notification"),
    INVITE_USER("invite-user"),
    USER_ADDED("user-added-to-group"),
    TANK_REFILL_NOTIFICATION("tank-refill-notification"),
    REGISTER_USER("register-user");
}