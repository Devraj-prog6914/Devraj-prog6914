import express from "express";
import twilio from "twilio";

const app = express();
app.use(express.json());

// Get credentials from environment variables (Railway → Variables)
const accountSid = process.env.TWILIO_SID;
const authToken = process.env.TWILIO_AUTH;
const twilioPhone = process.env.TWILIO_PHONE;

const client = twilio(accountSid, authToken);

// Test route
app.get("/", (req, res) => {
    res.send("🚀 Twilio SMS OTP backend running!");
});

// Send OTP API
app.post("/send-otp", async (req, res) => {
    const { phone } = req.body;
    const otp = Math.floor(100000 + Math.random() * 900000); // 6-digit OTP

    try {
        await client.messages.create({
            body: `Your OTP is: ${otp}`,
            from: twilioPhone,
            to: phone,
        });

        res.json({ success: true, otp }); // ⚠️ In production don’t send OTP back
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});
app.post("/verify-otp", (req, res) => {
    const { phone, otp } = req.body;

    if (!phone || !otp) {
        return res.status(400).json({ error: "Phone and OTP are required" });
    }

    if (otpStore[phone] && otpStore[phone] === otp) {
        delete otpStore[phone]; // OTP used → delete
        return res.json({ message: "OTP verified successfully" });
    } else {
        return res.status(400).json({ error: "Invalid OTP" });
    }
});

// Railway will auto-assign PORT
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`✅ Server running on port ${PORT}`));

