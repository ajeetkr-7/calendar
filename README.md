## Calendar Booking System

### Assumptions
1. Time slot of 23:00 to 00:00 (next day) is not being considered for simplicity.
    - this means user can create availability rule for 00:00 to 23:00 on a given day.
    - and also can book appointment between this time range.
2. User can create multiple availability rules for a given day with overlapping time slots.
    - e.g. user can create availability rule for 9AM to 12AM and 10AM to 2PM on a given day.
    - I have incorporated this flexibility by observing cal.com availability rules.

### UseCases
**Owner**
1. owner can set availability
2. owner can view the appointments made with him/her

**Attendee**
1. user can search available slots for a given owner on a specific date
2. user can book an appointment against an available slot

### Domain entities
1. User
2. Appointment - MON - 9AM - 10AM
3. AvailabilityRule - Mon - 9AM - 5PM

### Domain Model

**User**
- id
- name
- email - unique for each user

**Appointment**
- id
- userId
- inviteeEmail
- inviteeName
- startTime
- endTime

**AvailabilityRule**
- id
- userId
- weekday
- startTime
- endTime