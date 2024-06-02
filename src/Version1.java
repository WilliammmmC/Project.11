import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Version1 {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Welcome to the Hong Kong Tax Calculator!");
        System.out.println("This program will help you calculate your personal income tax rate for the 2023-2024 income tax cycle");
        System.out.println("To do so, this program will ask for information related to your income,household,and demographics.");
        System.out.println("Please note that the program assumes its user to have prior experiences with tax filing and hence, does not provide clarifications on specific terminology.");
        System.out.println("Should you have any questions or concerns, please consult the Inland Revenue Department website before filing for the specific section.");
        System.out.println("To begin, please input your net income tax amount.");
        int totalIncome = readInt();
        int taxReduction = 3000; //Value taken from the Internal Revenue Department (IRD)
        System.out.println(taxReduction);
        boolean inputCorrect = false;
        int totalDeduction = 0;
        int totalAllowance = 0;
        while (!inputCorrect) {
            int basicAllowance = 132000;
            System.out.println("Are you married?");
            boolean married = readBoolean();
            int marriageAllowance = marriageAllowance(married);
            System.out.println("Do you have any children?");
            boolean children = readBoolean();

            int childrenAllowance = childrenAllowance(children);
            System.out.println("Do you have any dependent sister or brother"); // removed additional clarification for enhanced readability
            boolean dependent = readBoolean();
            int sisterAllowance = sisterAllowance(dependent);

            System.out.println("Do you have any dependent parent or grandparent.");
            boolean parent = readBoolean();
            int parentAllowance = parentAllowance(parent);
            boolean singleParent;
            if (!married && children) {
                singleParent = true;
            } else {
                singleParent = false;
            }
            int singleAllowance = singleAllowance(singleParent);
            System.out.println("Do you/your spouse maintain a dependent who is eligible for the Government's Disability Allowance Scheme?");
            System.out.println("Even if you have already identified the dependent in one or more of the preceding questions, you can and should still identify them now.");
            boolean disabledDependent = readBoolean();

            int disabledDependentAllowance = disabledDependentAllowance(disabledDependent);
            System.out.println("Do you qualify for the Government's Disability Allowance Scheme?");
            boolean disabled = readBoolean();
            int disabledAllowance;
            if (disabled) {
                disabledAllowance = 75000;
            } else {
                disabledAllowance = 0;
            }

            totalAllowance = basicAllowance + marriageAllowance + childrenAllowance + sisterAllowance + parentAllowance + singleAllowance + disabledDependentAllowance + disabledAllowance;
            int[] deduction = new int[9];
            System.out.println("Please identify the amount you spent on tuition and related examination to obtain or maintain qualification for a current or planned employment.");
            deduction[0] = readInt();
            if (deduction[0] > 100000) { // Maximum deduction amount for education and tuition
                deduction[0] = 100000;
            }

            System.out.println("Please identify the amount of outgoing expenses you spent that is exclusively and necessarily for the production of your salary.");
            deduction[1] = readInt();
            if (deduction[1] > 100000) { //Maximum deduction amount for outgoing expenses
                deduction[1] = 100000;
            }

            System.out.println("Did you make any premium payment to a qualified insurance plan under the Voluntary Health Insurance Scheme Policy.");
            boolean VHISDeduction = readBoolean();
            deduction[2] = VHISDeduction(VHISDeduction);
            System.out.println("Please identify the amount of contribution paid to a recognized retirement scheme and the amount of mandatory contribution made to a Mandatory Fund Provident Scheme.");
            deduction[3] = readInt();
            if (deduction[3] > 18000) { //Maximum deduction amount for mandatory contribution
                deduction[3] = 18000;
            }

            System.out.println("Please identify the amount of premium and voluntary contribution paid to a qualified annuity policy for you and/or your spouse or a TVC account defined under the Mandatory Provident Fund Scheme that belongs to you.");
            deduction[4] = readInt();
            if (deduction[4] > 60000) { //Maximum deduction amount for voluntary contribution
                deduction[4] = 60000;
            }

            System.out.println("Please identify the amount of rent paid for your residence.");
            deduction[5] = readInt();
            if (deduction[5] > 100000) { //Maximum deduction amount for rent
                deduction[5] = 100000;
            }

            System.out.println("Please identify the amount of mortgage paid to a dwelling and attached parking spaces in Hong Kong.");
            deduction[6] = readInt();
            if (deduction[6] > 100000) { //Maximum deduction amount for mortgage
                deduction[6] = 100000;
            }

            System.out.println("Please identify the amount of expenses paid by you/your spouse for a residential care home for your/your spouse's parents/grandparents, provided that they are over the age of 60 or eligible for Government Disability Allowance Scheme.");
            deduction[7] = readInt();
            if (deduction[7] > 100000) { //Maximum deduction amount for dependency care
                deduction[7] = 100000;
            }

            System.out.println("Please identify the amount of donation paid to a tax-exempt charity organization or the government for a charitable cause.");
            deduction[8] = readInt();
            if (deduction[8] > totalIncome * 0.35) { //Maximum deduction amount for charitable donation
                deduction[8] = (int) (totalIncome * 0.35);
            }

            totalDeduction = totalDeduction(deduction);
            System.out.println("Is the information provided accurate and complete?");
            inputCorrect = readBoolean();
        }

        int netChargeable = totalIncome - totalDeduction - totalAllowance;
        if (netChargeable <= 0) {
            System.out.println("You do not need to pay any personal income tax for the year 2023 - 2024.");
            System.exit(0);

        }

        double taxProgressive = taxProgressive(netChargeable);
        double taxFlat = taxFlat(totalIncome);
        double taxFinal;
        if (taxProgressive > taxFlat) {
            taxFinal = taxFlat;
        } else {
            taxFinal = taxProgressive;
        }

        if (taxFinal <= 0) {
            System.out.println("You do not need to pay any personal income tax for the year 2023 - 2024.");
            System.exit(0);
        }

        System.out.println("Thank you so much for using the HK Tax Calculator");
        System.out.println("According to the information provided, your personal tax for the year 2023 - 2024 is: $" + taxFinal);
        System.out.println("Do you want to store your information into a file?");
        boolean storeFile = readBoolean();
        if (storeFile) {
            PrintStream ps = new PrintStream("userFile");
            ps.println("Total Income: " + totalIncome);
            ps.println("Total Tax Reduction in 2023-2024: " + taxReduction);
            ps.println("Total Eligible Allowances: " + totalAllowance);
            ps.println("Total Eligible Deductions: " + totalDeduction);
            ps.println("Total Taxable Income: " + netChargeable);
            if (taxProgressive > taxFlat) {
                ps.println("Type of Tax Rate Applied: Standard Flat rate");
            } else {
                ps.println("Type of Tax Rate Applied: Standard Progressive rate");
            }
            ps.println("Total Personal Tax Payment Due: " + taxFinal);
        }


    }

    public static int marriageAllowance(boolean married) {
        if (married) {
            return 264000;
        } else {
            return 0;
        }
    }

    public static int childrenAllowance(boolean children) {
        if (children) {
            System.out.println("How many children do you have");
            int childrenNumber = readInt();
            System.out.println("How many of your children are born in between 2023 and 2024?");
            int childrenIn = readInt();
            return childrenNumber * 130000 + childrenIn * 130000;
        } else {
            return 0;
        }
    }

    public static int sisterAllowance(boolean dependent) {
        if (dependent) {
            System.out.println("How many dependent sister and/or brother do you have");
            int sisterNumber = readInt();
            return sisterNumber * 37500;
        } else {
            return 0;
        }
    }

    public static int parentAllowance(boolean parent) {
        if (parent) {
            System.out.println("How many dependents parent and/or grandparents currently age over 60 (or is eligible for the Government's Disability Allowance Scheme) and live with you continuously throughout the whole year?");
            int sixtyAdditional = readInt();
            System.out.println("How many dependents parent and/or grandparents currently age above 55 and below 60 and live with you continuously throughout the whole year?");
            int fiftyAdditional = readInt();
            System.out.println("How many dependent parent or grandparent over the age of 60 (or is eligible for the Government's Disability Allowance Scheme) live with you for more than half a year continuously, but not the entire year?");
            int sixty = readInt();
            System.out.println("How many dependent parent or grandparent over the age of 55 and below the age of 60 live with you for more than half a year continuously, but not the entire year?");
            int fifty = readInt();
            return sixtyAdditional * 100000 + fiftyAdditional * 50000 + sixty * 50000 + fifty * 25000;
        } else {
            return 0;
        }
    }

    public static int singleAllowance(boolean single) {
        if (single) {
            return 132000;
        } else {
            return 0;
        }
    }

    public static int disabledDependentAllowance(boolean dependent) {
        if (dependent) {
            System.out.println("How many dependent are eligible for the Government's Disability Allowance Scheme?");
            System.out.println("Please identify all eligible dependent even if you have already identified them in preceding questions");
            int disabledDependent = readInt();
            return disabledDependent * 75000;
        } else {
            return 0;
        }
    }

    public static int VHISDeduction(boolean deduction) {
        if (deduction) {
            System.out.println("How many people did you make payment for?");
            System.out.println("Please include yourself, your spouse, your and your spouse's dependents in your calculations");
            int number = readInt();
            System.out.println("How much insurance payment did you make in total?");
            int payment = readInt();
            if (payment > number * 8000) {
                payment = number * 8000;
            }
            return payment;
        } else {
            return 0;
        }
    }

    public static int totalDeduction(int[] deductions) {
        int total = 0;
        for (int i = 0; i < deductions.length; i++) {
            total += deductions[i];
        }
        return total;
    }

    public static double taxProgressive(int amount) {
        if (amount <= 50000) { //bracketed approach to identify the correct income bracket
            return amount * 0.02;
        } else if (amount <= 100000) {
            double above50000 = amount - 50000;
            return 1000 + above50000 * 0.06;
        } else if (amount <= 150000) {
            double above100000 = amount - 100000;
            return 4000 + above100000 * 0.10;
        } else if (amount <= 200000) {
            double above150000 = amount - 150000;
            return 9000 + above150000 * 0.14;
        } else {
            double above200000 = amount - 200000;
            return 16000 + above200000 * 0.17;
        }
    }

    public static double taxFlat(int amount) {
        return amount * 0.15;
    }

    public static int readInt() { //Taught by Justin
        int result = -1;
        while (true) {
            System.out.println("Please enter an integer input:");
            String s = scanner.next().trim();
            try {
                result = Integer.parseInt(s);
            } catch (Exception e) {
                System.out.println("Invalid input. Please re-enter a valid integer.");
            }
            if (result != -1) break;
        }
        return result;
    }

    public static boolean readBoolean() { //Try-Catch Function from w3schools)
        Scanner scanner = new Scanner(System.in);
        boolean result = false;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter true if yes and false if no: ");
                result = scanner.nextBoolean();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
                scanner.nextLine();
            }
        }

        return result;
    }


}
