import java.util.*;
import java.time.LocalDate;

public class Main {

    static class Student {
        String uid;
        String name;
        int fineAmount;
        int currentBorrowCount;

        public Student(String uid, String name, int fineAmount, int currentBorrowCount) {
            this.uid = uid;
            this.name = name;
            this.fineAmount = fineAmount;
            this.currentBorrowCount = currentBorrowCount;
        }

        public void verifyEligibility() {
            if (fineAmount > 0) {
                throw new IllegalStateException("Outstanding dues detected for this student.");
            }
            if (currentBorrowCount >= 2) {
                throw new IllegalStateException("Maximum borrowing capacity reached.");
            }
        }
    }

    static class Asset {

        String assetId;
        String assetName;
        boolean available;
        int securityLevel;

        public Asset(String assetId, String assetName, boolean available, int securityLevel) {
            this.assetId = assetId;
            this.assetName = assetName;
            this.available = available;
            this.securityLevel = securityLevel;
        }

        public void verifyAccess(String uid) {
            if (!available) {
                throw new IllegalStateException("Requested asset is currently issued.");
            }

            if (securityLevel == 3 && !uid.startsWith("KRG")) {
                throw new SecurityException("High security clearance required for this asset.");
            }
        }
    }

    static class CheckoutRequest {
        String uid;
        String assetId;
        int hoursRequested;

        public CheckoutRequest(String uid, String assetId, int hoursRequested) {
            this.uid = uid;
            this.assetId = assetId;
            this.hoursRequested = hoursRequested;
        }
    }

    static class ValidationUtil {

        public static void checkUid(String uid) {
            if (uid == null || uid.length() < 8 || uid.length() > 12 || uid.contains(" ")) {
                throw new IllegalArgumentException("UID does not meet required format.");
            }
        }

        public static void checkAssetId(String assetId) {
            if (assetId == null || !assetId.startsWith("LAB-")) {
                throw new IllegalArgumentException("Asset ID should begin with LAB-.");
            }

            String digits = assetId.substring(4);
            for (int i = 0; i < digits.length(); i++) {
                if (!Character.isDigit(digits.charAt(i))) {
                    throw new IllegalArgumentException("Asset ID must contain digits after prefix.");
                }
            }
        }

        public static void checkHours(int hrs) {
            if (hrs < 1 || hrs > 6) {
                throw new IllegalArgumentException("Requested duration must be between 1 and 6 hours.");
            }
        }
    }

    static class AssetStore {

        HashMap<String, Asset> assetRegistry = new HashMap<>();

        public void registerAsset(Asset asset) {
            assetRegistry.put(asset.assetId, asset);
        }

        public Asset getAsset(String assetId) {
            Asset found = assetRegistry.get(assetId);

            if (found == null) {
                throw new NullPointerException("No asset available with ID: " + assetId);
            }

            return found;
        }

        public void updateStatusToBorrowed(Asset asset) {
            if (!asset.available) {
                throw new IllegalStateException("Asset already allocated.");
            }
            asset.available = false;
        }
    }

    static class CheckoutService {

        AssetStore store;
        HashMap<String, Student> studentDirectory;

        public CheckoutService(AssetStore store, HashMap<String, Student> studentDirectory) {
            this.store = store;
            this.studentDirectory = studentDirectory;
        }

        public String processCheckout(CheckoutRequest request)
                throws IllegalArgumentException, IllegalStateException,
                       SecurityException, NullPointerException {

            ValidationUtil.checkUid(request.uid);
            ValidationUtil.checkAssetId(request.assetId);
            ValidationUtil.checkHours(request.hoursRequested);

            Student student = studentDirectory.get(request.uid);
            if (student == null) {
                throw new NullPointerException("Student record not located.");
            }

            Asset asset = store.getAsset(request.assetId);

            student.verifyEligibility();
            asset.verifyAccess(request.uid);

            if (request.hoursRequested == 6) {
                System.out.println("Notice: You have selected the maximum allowable duration.");
            }

            if (asset.assetName.contains("Cable") && request.hoursRequested > 3) {
                request.hoursRequested = 3;
                System.out.println("Restriction Applied: Cable usage limited to 3 hours.");
            }

            store.updateStatusToBorrowed(asset);
            student.currentBorrowCount++;

            String date = LocalDate.now().toString().replace("-", "");
            return "RECEIPT-" + date + "-" + request.assetId + "-" + request.uid;
        }
    }

    static class AuditLogger {

        public static void record(String msg) {
            System.out.println("[LOG ENTRY] " + msg);
        }

        public static void recordError(Exception e) {
            System.out.println("[EXCEPTION] " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        Student st1 = new Student("KRG11771", "Arnav", 0, 0);
        Student st2 = new Student("ABC15456", "richa", 100, 0);
        Student st3 = new Student("KRG88999", "tarun", 0, 2);

        HashMap<String, Student> directory = new HashMap<>();
        directory.put(st1.uid, st1);
        directory.put(st2.uid, st2);
        directory.put(st3.uid, st3);

        Asset as1 = new Asset("LAB-101", "HDMI Cable", true, 1);
        Asset as2 = new Asset("LAB-102", "Oscilloscope", true, 3);
        Asset as3 = new Asset("LAB-103", "Projector", false, 2);

        AssetStore inventory = new AssetStore();
        inventory.registerAsset(as1);
        inventory.registerAsset(as2);
        inventory.registerAsset(as3);

        CheckoutService checkoutService = new CheckoutService(inventory, directory);

        CheckoutRequest q1 = new CheckoutRequest("KRG11771", "LAB-101", 5);
        CheckoutRequest q2 = new CheckoutRequest("KRG11771", "LAB-XYZ", 7);
        CheckoutRequest q3 = new CheckoutRequest("ABC12345", "LAB-102", 2);

        CheckoutRequest[] allRequests = { q1, q2, q3 };

        for (CheckoutRequest request : allRequests) {

            try {
                String result = checkoutService.processCheckout(request);
                System.out.println("TRANSACTION SUCCESSFUL → " + result);

            } catch (IllegalArgumentException e) {
                System.out.println("Input Error: " + e.getMessage());
                AuditLogger.recordError(e);

            } catch (NullPointerException e) {
                System.out.println("Data Missing: " + e.getMessage());
                AuditLogger.recordError(e);

            } catch (SecurityException e) {
                System.out.println("Authorization Failure: " + e.getMessage());
                AuditLogger.recordError(e);

            } catch (IllegalStateException e) {
                System.out.println("Policy Violation: " + e.getMessage());
                AuditLogger.recordError(e);

            } finally {
                AuditLogger.record("Processing completed for UID=" + request.uid +
                        ", AssetID=" + request.assetId);
            
            }
        }
    }
        } 
