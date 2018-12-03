import { IAddresses } from 'app/shared/model//addresses.model';

export interface ICountries {
    id?: number;
    countryName?: string;
    addresses?: IAddresses[];
}

export class Countries implements ICountries {
    constructor(public id?: number, public countryName?: string, public addresses?: IAddresses[]) {}
}
