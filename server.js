import express from "express";
import twilio from "twilio";

const app = express();
app.use(express.json());

// Twilio credentials
const accountSid = process.env.TWILIO_SID;
const authToken = process.env.TWILIO_AUTH;
const twilioPhone = process.env.TWILIO_PHONE;

const client = twilio(accountSid, authToken);

// OTP memory store
const otpStore = {};

// Test route
app.get("/", (req, res) => {
    res.send("🚀 Twilio SMS OTP backend running on Render!");
});

// Send OTP
app.post("/send-otp", async (req, res) => {
    const { phone } = req.body;

    if (!phone) {
        return res.status(400).json({ success: false, message: "Phone number required" });
    }

    const otp = Math.floor(100000 + Math.random() * 900000).toString();

    try {
        await client.messages.create({
            body: `Your OTP is: ${otp}`,
            from: twilioPhone,
            to: phone,
        });

        otpStore[phone] = { otp, expires: Date.now() + 5 * 60 * 1000 };

        res.json({ success: true, message: "OTP sent successfully" });
    } catch (error) {
        console.error("❌ Twilio Error:", error); // log in Render logs
        res.status(500).json({ success: false, error: error.message });
    }
});

// Verify OTP
app.post("/verify-otp", (req, res) => {
    const { phone, otp } = req.body;

    if (!otpStore[phone]) {
        return res.status(400).json({ success: false, message: "OTP not requested" });
    }

    const { otp: storedOtp, expires } = otpStore[phone];

    if (Date.now() > expires) {
        return res.status(400).json({ success: false, message: "OTP expired" });
    }

    if (storedOtp === otp) {
        delete otpStore[phone];
        return res.json({ success: true, message: "OTP verified successfully" });
    } else {
        return res.status(400).json({ success: false, message: "Invalid OTP" });
    }
});

// Render will assign PORT
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`✅ Server running on port ${PORT}`));
