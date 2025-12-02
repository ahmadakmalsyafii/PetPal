# Branch Selection Implementation

## Overview
Implemented branch selection functionality for the order form in PetPal app, matching the design requirements.

## Files Created/Modified

### 1. Created: BranchSelectionScreen.kt
**Location:** `app/src/main/java/com/example/petpal/presentation/view/BranchSelectionScreen.kt`

**Features:**
- Displays 3 branch options with location pin icons
- Branch cards with rounded corners and subtle shadows
- Each branch shows:
  - Branch name (e.g., "Cabang 1 : PetPal Prime")
  - Full address
  - Location icon (using `icon_branchoption_foreground.xml`)
- Header with "Pilih Cabang" title and back button
- Material 3 design with proper spacing and styling

**Branches Available:**
1. **Cabang 1 : PetPal Prime** - Taman Borobudur 1 Blok E Nomor 9, Kelapa Dua, Tangerang
2. **Cabang 2 : PetPal Next** - Jalan Sigura-Gura V Nomor 5, Lowokwaru, Malang
3. **Cabang 3 : PetPal Lux** - Jalan Camp Nou Nomor 6, Barcelona, Spain

### 2. Modified: Screen.kt
**Location:** `app/src/main/java/com/example/petpal/presentation/navigation/Screen.kt`

**Changes:**
- Added `BranchSelection` route to the Screen sealed class

### 3. Modified: OrderFormScreen.kt
**Location:** `app/src/main/java/com/example/petpal/presentation/view/OrderFormScreen.kt`

**Changes:**
- Fixed function signature with proper parameters
- Added `selectedBranchFromNav` parameter to receive selected branch from navigation
- Added `onNavigateToBranchSelection` callback
- Added missing state variables:
  - `selectedBranch` - stores selected branch
  - `endDate` - stores end date for duration
  - `note` - stores order notes
  - `isPriceExpanded` - controls price detail expansion
  - `showEndDatePicker` - controls end date picker visibility
- Updated Branch field to use `icon_branchoption_foreground` icon
- Added `DurationField` composable for date/time selection UI
- Fixed all missing callbacks and parameters

### 4. Modified: AppNavigation.kt
**Location:** `app/src/main/java/com/example/petpal/presentation/navigation/AppNavigation.kt`

**Changes:**
- Added import for `BranchSelectionScreen`
- Fixed function declaration syntax
- Added branch selection route with animations:
  - Slide in from bottom animation (300ms)
  - Slide out to bottom animation (300ms)
- Wired up branch selection in OrderForm:
  - Retrieves selected branch from saved state
  - Passes to OrderFormScreen via `selectedBranchFromNav`
  - Navigates to BranchSelection when branch field is clicked
  - Saves selected branch back to saved state
- Fixed all other navigation callbacks for PetSelection and TierSelection

## User Flow

1. User opens Order Form screen
2. User taps on "Cabang" field (with branch icon)
3. Branch Selection screen slides up from bottom
4. User sees list of 3 available branches
5. User taps on desired branch
6. Screen returns to Order Form with selected branch displayed
7. Selected branch persists when navigating between screens

## Design Features

- **Icon:** Uses `icon_branchoption_foreground.xml` as specified
- **Layout:** Matches the provided design with circular icon backgrounds
- **Colors:** Dark green circle background for icons, proper text hierarchy
- **Spacing:** Consistent 16dp padding and spacing
- **Typography:** Bold titles (16sp), gray addresses (14sp)
- **Interaction:** Cards are fully clickable with visual feedback

## Integration with Order System

The selected branch is:
- Stored in the order form state
- Passed to the Order data model (which has a `branch: String` field)
- Used for form completion validation
- Persisted through navigation using SavedStateHandle

## Next Steps (if needed)

- Add branch data from backend/database instead of hardcoded
- Add branch images/photos
- Add distance/directions functionality
- Add branch operating hours
- Add branch contact information

