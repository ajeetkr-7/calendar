## Calendar Booking System

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