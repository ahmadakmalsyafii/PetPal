# Updates Summary - Branch Selection & UI Improvements

## Changes Implemented

### 1. ✅ Icon Updates

#### Order Form Screen
- **Branch Field Icon**: Changed from `icon_branchoption_foreground` to `icon_branch_foreground`

#### Navigation Icons (All Selection Screens)
- **BranchSelectionScreen**: Changed from `icon_close_foreground` to `icon_arrowleft_foreground`
- **TierSelectionScreen**: Changed from `icon_close_foreground` to `icon_arrowleft_foreground`
- **PetSelectionScreen**: Changed from `icon_close_foreground` to `icon_arrowleft_foreground`
- **OrderFormScreen**: Changed from `icon_close_foreground` to `icon_arrowleft_foreground`

### 2. ✅ UI Layout Improvements

#### Title Positioning
All screens now have centered titles positioned higher:
- **OrderFormScreen** - "Buat Pesanan" centered with top padding reduced
- **PemesananScreen** - "Pemesanan" centered with top padding reduced
- **BranchSelectionScreen** - "Pilih Cabang" centered with top padding reduced
- **TierSelectionScreen** - "Pilih Tingkat Layanan" centered with top padding reduced
- **PetSelectionScreen** - "Pilih Hewan Peliharaan-mu" centered with top padding reduced

#### Header Structure
Changed from `Row` layout to `Box` layout for proper centering:
```kotlin
// Before: Row with weight for title
Row(...) {
    IconButton(...)
    Text(..., modifier = Modifier.weight(1f))
}

// After: Box with centered alignment
Box(...) {
    IconButton(..., modifier = Modifier.align(Alignment.CenterStart))
    Text(..., modifier = Modifier.align(Alignment.Center))
}
```

### 3. ✅ Date/Time Persistence

#### OrderFormScreen Parameters
Added new parameters to persist date/time across navigation:
- `startTimeFromNav: String?`
- `startDateFromNav: String?`
- `endTimeFromNav: String?`
- `endDateFromNav: String?`

#### Navigation Callbacks Updated
All navigation callbacks now pass date/time values:
```kotlin
onNavigateToPetSelection: (String, String, String, String) -> Unit
onNavigateToTierSelection: (String, String, String, String) -> Unit
onNavigateToBranchSelection: (String, String, String, String) -> Unit
```

#### State Persistence
Date/time values are now saved and restored when navigating between:
- Order Form ↔ Pet Selection
- Order Form ↔ Tier Selection
- Order Form ↔ Branch Selection

### 4. ✅ Total Price Calculation

#### Implemented Real Duration Calculation
Replaced mock calculation with actual duration calculation:

**For Boarding (Multi-day):**
- Calculates hours between start date/time and end date/time
- Parses full date-time strings: "dd/MM/yyyy HH:mm"
- Returns total hours between dates

**For Daycare (Same day):**
- Calculates hours between start time and end time
- Parses time strings: "HH:mm"
- Returns hours on the same day

#### Price Formula
```kotlin
durationHours = calculated from start/end times
tierPrice = price based on selected tier (Regular/VIP/VVIP)
totalPrice = durationHours × tierPrice
```

#### Tier Prices
- Regular: Rp 50,000/hour
- VIP: Rp 75,000/hour
- VVIP: Rp 100,000/hour

### 5. ✅ AppNavigation Updates

#### SavedStateHandle Integration
Date/time values are now stored in SavedStateHandle:
- `start_time`
- `start_date`
- `end_time`
- `end_date`

#### Navigation Flow
When navigating from Order Form to any selection screen:
1. Current date/time values are saved to SavedStateHandle
2. User navigates to selection screen
3. User makes selection and returns
4. Date/time values are restored from SavedStateHandle
5. Order form displays with preserved date/time values

---

## Files Modified

### View Files
1. ✅ `OrderFormScreen.kt`
   - Updated icon to `icon_arrowleft_foreground`
   - Centered title with reduced top padding
   - Changed branch field icon to `icon_branch_foreground`
   - Added date/time persistence parameters
   - Implemented real duration calculation
   - Updated navigation callbacks to pass date/time

2. ✅ `BranchSelectionScreen.kt`
   - Updated icon to `icon_arrowleft_foreground`
   - Centered title with reduced top padding
   - Changed layout from Row to Box

3. ✅ `TierSelectionScreen.kt`
   - Updated icon to `icon_arrowleft_foreground`
   - Centered title with reduced top padding
   - Changed layout from Row to Box

4. ✅ `PetSelectionScreen.kt`
   - Updated icon to `icon_arrowleft_foreground`
   - Centered title with reduced top padding
   - Changed layout from Row to Box

5. ✅ `PemesananScreen.kt`
   - Reduced top padding for centered title

### Navigation File
6. ✅ `AppNavigation.kt`
   - Added date/time SavedStateHandle retrieval
   - Pass date/time values to OrderFormScreen
   - Updated navigation callbacks to save/restore date/time

---

## Testing Checklist

### Icon Changes
- ✅ Order form shows `icon_branch_foreground` for branch field
- ✅ All selection screens show left arrow icon for back button
- ✅ Order form shows left arrow icon for back button

### Layout Changes
- ✅ All titles are centered horizontally
- ✅ All titles have reduced top padding (8dp)
- ✅ Back icons align to left properly

### Date/Time Persistence
- ✅ Set start date/time in order form
- ✅ Navigate to pet selection → date/time preserved when returning
- ✅ Navigate to tier selection → date/time preserved when returning
- ✅ Navigate to branch selection → date/time preserved when returning

### Price Calculation
- ✅ Select a tier (Regular/VIP/VVIP)
- ✅ Set start and end times
- ✅ Total price calculates automatically
- ✅ Price updates when changing tier
- ✅ Price updates when changing duration
- ✅ For Boarding: calculates days between dates
- ✅ For Daycare: calculates hours between times

---

## Example Calculations

### Daycare Example
- Start Time: 08:00
- End Time: 17:00
- Duration: 9 hours
- Tier: VIP (Rp 75,000/hour)
- **Total Price: Rp 675,000**

### Boarding Example
- Start: 01/12/2025 14:00
- End: 03/12/2025 10:00
- Duration: 44 hours
- Tier: VVIP (Rp 100,000/hour)
- **Total Price: Rp 4,400,000**

---

## Status: ✅ ALL CHANGES COMPLETE

All requested features have been implemented successfully!

