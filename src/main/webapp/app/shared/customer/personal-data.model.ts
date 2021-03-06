import { Moment } from 'moment';
import { IOrders } from 'app/shared/model//orders.model';
import { IAddresses } from 'app/shared/model//addresses.model';
import { IProducts } from 'app/shared/model//products.model';

export const enum GenderEnum {
    MASCULINO = 'MASCULINO',
    FEMININO = 'FEMININO'
}

export interface IPersonalData {
    id?: number;
    customerName?: string;
    customerEmail?: string;
    customerNif?: string;
    customerPhone?: string;
    customerGender?: GenderEnum;
    customerBeginDate?: Moment;
    customerEndDate?: Moment;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    login?: string;
    orders?: IOrders[];
    addresses?: IAddresses[];
    products?: IProducts[];
}

export class PersonalData implements IPersonalData {
    constructor(
        public id?: number,
        public customerName?: string,
        public customerEmail?: string,
        public customerNif?: string,
        public customerPhone?: string,
        public customerGender?: GenderEnum,
        public customerBeginDate?: Moment,
        public customerEndDate?: Moment,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public login?: string,
        public orders?: IOrders[],
        public addresses?: IAddresses[],
        public products?: IProducts[]
    ) {}
}
