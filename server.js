import express from "express";
import cors from "cors";
import twilio from "twilio";

const app = express();
app.use(express.json());
app.use(cors()); // ✅ allow Android requests

// 🔑 Twilio creds from Railway Environment Variables
const accountSid = process.env.TWILIO_SID;
const authToken = process.env.TWILIO_AUTH;
const twilioPhone = process.env.TWILIO_PHONE;

const client = twilio(accountSid, authToken);

// In-memory OTP store
const otpStore = {};

// Test route
app.get("/", (req, res) => {
    res.send("🚀 Twilio SMS OTP backend running!");
});

// ✅ Send OTP
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

        // store OTP temporarily (valid for 5 mins)
        otpStore[phone] = { otp, expires: Date.now() + 5 * 60 * 1000 };

        res.json({ success: true, message: "OTP sent successfully" });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// ✅ Verify OTP
app.post("/verify-otp", (req, res) => {
    const { phone, otp } = req.body;

    if (!phone || !otp) {
        return res.status(400).json({ success: false, message: "Phone & OTP required" });
    }

    const record = otpStore[phone];

    if (record && record.otp === otp && record.expires > Date.now()) {
        delete otpStore[phone]; // clear OTP after success
        return res.json({ success: true, message: "OTP verified successfully" });
    }

    return res.status(400).json({ success: false, message: "Invalid or expired OTP" });
});

// Railway PORT
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`✅ Server running on port ${PORT}`));
