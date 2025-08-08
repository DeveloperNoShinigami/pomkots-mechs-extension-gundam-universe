Goal:
- Implement Gundam mech entity data tracking and serialization.
Outcome:
- Energy, pilot status and equipment now sync and persist; serialization verified.
What went well:
- Leveraged synched data for custom fields.
- Added unit test to confirm NBT round-trip.
What went wrong:
- Lacked direct access to base mod sources, requiring assumptions about abstract methods.
Improvements:
- Investigate upstream API to flesh out combat and weapon logic.
