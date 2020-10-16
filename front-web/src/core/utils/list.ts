export const generateList = (amount: number) => {
    return Array.from(Array(amount).keys());//criamos array passando-lhe as keys de tantos elementos quantos queremos nesse array
}