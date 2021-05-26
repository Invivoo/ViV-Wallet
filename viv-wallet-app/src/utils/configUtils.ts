export default function getConfigValue(name: string) {
    return window?.configs?.[name] || process.env[name];
}
